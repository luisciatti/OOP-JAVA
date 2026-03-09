package view;

import controller.MidiaController;
import model.Midia;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.List;

/**
 * Tela principal da aplicação para gerenciamento de mídias.
 * <p>
 * Esta classe exibe a lista de mídias cadastradas, além de fornecer
 * botões para executar ações como adicionar, editar, remover,
 * mover arquivos, renomear arquivos, filtrar e exibir detalhes.
 * <p>
 * A comunicação com a lógica de negócios é feita através do {@link MidiaController}.
 */
public class TelaPrincipal extends JFrame {

    private static final long serialVersionUID = 1L;

    /** Controlador responsável pelas operações de negócio. */
    private MidiaController controller;

    /** Modelo que mantém os elementos exibidos no JList. */
    private DefaultListModel<Midia> listModel;

    /** Lista gráfica que exibe as mídias cadastradas. */
    private JList<Midia> jlist;

    /**
     * Construtor da tela principal.
     *
     * @param controller controlador responsável pela lógica da aplicação
     */
    public TelaPrincipal(MidiaController controller) {
        super("Gerenciador de Mídias");
        this.controller = controller;
        setupUI();
    }

    /**
     * Configura toda a interface gráfica da janela:
     * <ul>
     *     <li>Lista central contendo as mídias</li>
     *     <li>Painel inferior com botões de ações</li>
     * </ul>
     * Não deve ser chamado fora do construtor.
     */
    private void setupUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLayout(new BorderLayout());

        /* LISTA DE MÍDIAS */
        listModel = new DefaultListModel<>();
        jlist = new JList<>(listModel);
        jlist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        add(new JScrollPane(jlist), BorderLayout.CENTER);

        /* PAINEL DE BOTÕES */
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT));

        /* Botão Adicionar */
        JButton btnAdd = new JButton("Adicionar");
        btnAdd.addActionListener(e -> controller.acaoAdicionar());
        bottom.add(btnAdd);

        /* Botão Editar */
        JButton btnEditar = new JButton("Editar");
        btnEditar.addActionListener(e -> {
            Midia selecionada = jlist.getSelectedValue();
            if (selecionada == null) {
                mostrarMensagem("Selecione uma mídia para editar.");
                return;
            }
            controller.acaoEditar(selecionada);
        });
        bottom.add(btnEditar);

        /* Botão Remover */
        JButton btnRemover = new JButton("Remover");
        btnRemover.addActionListener(e -> {
            Midia selecionada = jlist.getSelectedValue();
            if (selecionada == null) {
                mostrarMensagem("Selecione uma mídia para remover.");
                return;
            }

            int conf = JOptionPane.showConfirmDialog(
                    this, "Confirma remoção?", "Remover", JOptionPane.YES_NO_OPTION);

            if (conf == JOptionPane.YES_OPTION) {
                controller.acaoRemover(selecionada);
            }
        });
        bottom.add(btnRemover);

        /* Botão Mover Arquivo */
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

        /* Botão Renomear Arquivo */
        JButton btnRenomear = new JButton("Renomear");
        btnRenomear.addActionListener(e -> {
            Midia selecionada = jlist.getSelectedValue();
            if (selecionada == null) {
                mostrarMensagem("Selecione uma mídia para renomear.");
                return;
            }

            String novoNome = JOptionPane.showInputDialog(
                    this, "Novo nome (com extensão):", "");

            if (novoNome != null && !novoNome.trim().isEmpty()) {
                controller.acaoRenomear(selecionada, novoNome.trim());
            }
        });
        bottom.add(btnRenomear);

        /* Botão Filtrar e Ordenar */
        JButton btnFiltrar = new JButton("Filtrar (Formato/Categoria)");
        btnFiltrar.addActionListener(e -> {

            String[] tipos = {"Todos", "Filme", "Musica", "Livro"};
            String tipoSel = (String) JOptionPane.showInputDialog(
                    this, "Formato:", "Filtro",
                    JOptionPane.PLAIN_MESSAGE, null, tipos, tipos[0]
            );

            Class<?> tipo = null;
            if ("Filme".equals(tipoSel)) tipo = model.Filme.class;
            else if ("Musica".equals(tipoSel)) tipo = model.Musica.class;
            else if ("Livro".equals(tipoSel)) tipo = model.Livro.class;

            String categoria = JOptionPane.showInputDialog(
                    this, "Categoria (deixe vazio para todos):");
            if (categoria != null && categoria.trim().isEmpty()) categoria = null;

            String[] ords = {"nenhuma", "titulo", "duracao"};
            String ord = (String) JOptionPane.showInputDialog(
                    this, "Ordenar por:", "Ordenar",
                    JOptionPane.PLAIN_MESSAGE, null, ords, ords[0]
            );

            controller.aplicarFiltro(tipo, categoria, ord);
        });
        bottom.add(btnFiltrar);

        /* Botão Exibir Detalhes */
        JButton btnDetalhes = new JButton("Exibir Detalhes");
        btnDetalhes.addActionListener(e -> {
            Midia selecionada = jlist.getSelectedValue();
            if (selecionada == null) {
                mostrarMensagem("Selecione uma mídia para ver os detalhes.");
                return;
            }

            JOptionPane.showMessageDialog(
                    this, selecionada.exibirDetalhes(),
                    "Detalhes da Mídia", JOptionPane.INFORMATION_MESSAGE
            );
        });
        bottom.add(btnDetalhes);

        add(bottom, BorderLayout.SOUTH);
    }

    /**
     * Exibe a janela principal na Event Dispatch Thread.
     */
    public void mostrar() {
        SwingUtilities.invokeLater(() -> setVisible(true));
    }

    /**
     * Atualiza a lista exibida na interface com o conteúdo fornecido.
     *
     * @param midias lista de mídias atualizada
     */
    public void atualizarLista(List<Midia> midias) {
        SwingUtilities.invokeLater(() -> {
            listModel.clear();
            for (Midia m : midias) listModel.addElement(m);
        });
    }

    /**
     * Abre o {@link FormMidia} para criação ou edição de uma mídia.
     *
     * @param m mídia existente (edição) ou {@code null} para criação
     * @return mídia criada/editada ou {@code null} caso o usuário cancele
     */
    public Midia pedirDadosEdicao(Midia m) {
        FormMidia form = new FormMidia(this, m == null ? null : m.getClass());
        return form.mostrar(m);
    }

    /**
     * Exibe uma mensagem simples em um {@link JOptionPane}.
     *
     * @param msg texto da mensagem
     */
    public void mostrarMensagem(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }

    /**
     * Abre um {@link JFileChooser} para selecionar um arquivo.
     *
     * @return arquivo selecionado ou {@code null} se cancelado
     */
    public File pedirEntradaNovoArquivo() {
        JFileChooser chooser = new JFileChooser();
        int res = chooser.showOpenDialog(this);
        if (res == JFileChooser.APPROVE_OPTION) {
            return chooser.getSelectedFile();
        }
        return null;
    }
}
