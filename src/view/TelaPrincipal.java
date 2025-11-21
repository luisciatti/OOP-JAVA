package view;

import controller.MidiaController;
import model.Midia;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.List;

//Tela principal da aplicação.
//Responsável por exibir a lista de mídias e oferecer botões
// para executar as ações do controlador (adicionar, editar, remover, mover, etc.).

public class TelaPrincipal extends JFrame {

    private MidiaController controller;   // referência ao controller que executa a lógica
    private DefaultListModel<Midia> listModel;  // modelo da lista
    private JList<Midia> jlist;           // lista gráfica onde as mídias aparecem

    // Construtor da tela principal.
    // Inicializa a interface chamando setupUI().
     
    public TelaPrincipal(MidiaController controller) {
        super("Gerenciador de Mídias");
        this.controller = controller;
        setupUI();
    }

    // Monta toda a interface gráfica:
    // Lista central com as mídias
    // Barra inferior com botões de ações
     
    private void setupUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLayout(new BorderLayout());

        /* ---------- LISTA DE MÍDIAS ---------- */
        listModel = new DefaultListModel<>();
        jlist = new JList<>(listModel);
        jlist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // adiciona a lista com scroll
        add(new JScrollPane(jlist), BorderLayout.CENTER);

        /* ---------- PAINEL DE BOTÕES ---------- */
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT));

        // Botão de adicionar
        JButton btnAdd = new JButton("Adicionar");
        btnAdd.addActionListener(e -> controller.acaoAdicionar());
        bottom.add(btnAdd);

        // Botão de editar
        JButton btnEditar = new JButton("Editar");
        btnEditar.addActionListener(e -> {
            // pega o item selecionado
            Midia selecionada = jlist.getSelectedValue();
            if (selecionada == null) {
                mostrarMensagem("Selecione uma mídia para editar.");
                return;
            }
            controller.acaoEditar(selecionada);
        });
        bottom.add(btnEditar);

        // Botão remover
        JButton btnRemover = new JButton("Remover");
        btnRemover.addActionListener(e -> {
            Midia selecionada = jlist.getSelectedValue();
            if (selecionada == null) {
                mostrarMensagem("Selecione uma mídia para remover.");
                return;
            }

            // confirmação simples
            int conf = JOptionPane.showConfirmDialog(this, 
                    "Confirma remoção?", 
                    "Remover", 
                    JOptionPane.YES_NO_OPTION);

            if (conf == JOptionPane.YES_OPTION) {
                controller.acaoRemover(selecionada);
            }
        });
        bottom.add(btnRemover);

        // Botão mover arquivo
        JButton btnMover = new JButton("Mover");
        btnMover.addActionListener(e -> {
            Midia selecionada = jlist.getSelectedValue();
            if (selecionada == null) {
                mostrarMensagem("Selecione uma mídia para mover.");
                return;
            }

            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

            int res = chooser.showOpenDialog(this);
            if (res == JFileChooser.APPROVE_OPTION) {
                File dir = chooser.getSelectedFile();
                controller.acaoMover(selecionada, dir.getAbsolutePath());
            }
        });
        bottom.add(btnMover);

        // Botão renomear arquivo
        JButton btnRenomear = new JButton("Renomear");
        btnRenomear.addActionListener(e -> {
            Midia selecionada = jlist.getSelectedValue();
            if (selecionada == null) {
                mostrarMensagem("Selecione uma mídia para renomear.");
                return;
            }

            // entrada de texto simples
            String novoNome = JOptionPane.showInputDialog(this, 
                    "Novo nome (com extensão):", "");

            if (novoNome != null && !novoNome.trim().isEmpty()) {
                controller.acaoRenomear(selecionada, novoNome.trim());
            }
        });
        bottom.add(btnRenomear);

        /* ---------- FILTRO + ORDENAÇÃO ---------- */
        JButton btnFiltrar = new JButton("Filtrar (Formato/Categoria)");
        btnFiltrar.addActionListener(e -> {

            // seleção de tipo
            String[] tipos = {"Todos", "Filme", "Musica", "Livro"};
            String tipoSel = (String) JOptionPane.showInputDialog(this,
                    "Formato:", "Filtro",
                    JOptionPane.PLAIN_MESSAGE, null,
                    tipos, tipos[0]);

            Class<?> tipo = null;
            if ("Filme".equals(tipoSel)) tipo = model.Filme.class;
            else if ("Musica".equals(tipoSel)) tipo = model.Musica.class;
            else if ("Livro".equals(tipoSel)) tipo = model.Livro.class;

            // Categoria opcional
            String categoria = JOptionPane.showInputDialog(this, 
                    "Categoria (deixe vazio para todos):");
            if (categoria != null && categoria.trim().isEmpty()) categoria = null;

            // Ordem simples
            String[] ords = {"nenhuma", "titulo", "duracao"};
            String ord = (String) JOptionPane.showInputDialog(this,
                    "Ordenar por:", "Ordenar",
                    JOptionPane.PLAIN_MESSAGE, null,
                    ords, ords[0]);

            controller.aplicarFiltro(tipo, categoria, ord);
        });
        bottom.add(btnFiltrar);
        
      // Botão para Exibir os detalhes
        JButton btnDetalhes = new JButton("Exibir Detalhes");
        btnDetalhes.addActionListener(e -> {
            Midia selecionada = jlist.getSelectedValue();
            if (selecionada == null) {
                mostrarMensagem("Selecione uma mídia para ver os detalhes.");
                return;
            }

            JOptionPane.showMessageDialog(this,
                    selecionada.exibirDetalhes(),
                    "Detalhes da Mídia",
                    JOptionPane.INFORMATION_MESSAGE);
        });
        bottom.add(btnDetalhes);

        
       

        add(bottom, BorderLayout.SOUTH);
    }

    //Mostra a janela (UI Thread).
     
    public void mostrar() {
        SwingUtilities.invokeLater(() -> setVisible(true));
    }

   
     // Atualiza a lista de mídias exibida na interface.
     
    public void atualizarLista(List<Midia> midias) {
        SwingUtilities.invokeLater(() -> {
            listModel.clear();
            for (Midia m : midias) listModel.addElement(m);
        });
    }

    
     //Abre o formulário FormMidia para edição ou criação.
    
    public Midia pedirDadosEdicao(Midia m) {
        FormMidia form = new FormMidia(this, m == null ? null : m.getClass());
        return form.mostrar(m);
    }
    //Exibe uma mensagem simples.
     
    public void mostrarMensagem(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }

    // Abre um JFileChooser para escolher um novo arquivo.
     
    public File pedirEntradaNovoArquivo() {
        JFileChooser chooser = new JFileChooser();
        int res = chooser.showOpenDialog(this);
        if (res == JFileChooser.APPROVE_OPTION) {
            return chooser.getSelectedFile();
        }
        return null;
    }
}
