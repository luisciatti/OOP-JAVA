package controller;

import model.GerenciadorMidias;
import model.Midia;
import view.TelaPrincipal;

import java.util.List;

// Controlador responsável pela comunicação entre a interface gráfica (view)
// e o GerenciadorMidias, que contém as regras de negócio.
 

public class MidiaController {

    // gerencia todas as operações com as mídias (negócio)
    private GerenciadorMidias gerenciador;

    // janela principal da interface
    private TelaPrincipal viewPrincipal;

    //Construtor que recebe a view e o gerenciador.
    
    public MidiaController(GerenciadorMidias gerenciador, TelaPrincipal view) {
        this.gerenciador = gerenciador;
        this.viewPrincipal = view;
    }
    
    //Inicializa o sistema:
    // carrega mídias salvas no diretório
    // atualiza a lista da view
    // exibe a tela principal
     
    public void inicializar() {
        gerenciador.carregarMidias();
        atualizarListaView();
        viewPrincipal.mostrar();
    }

    //Ação do botão "Adicionar".
    //Abre o formulário e, caso seja preenchido corretamente,
    // adiciona a nova mídia no gerenciador.
     
    public void acaoAdicionar() {
        // usa o mesmo form de edição para criar nova mídia
        Midia nova = viewPrincipal.pedirDadosEdicao(null);

        if (nova != null) {
            gerenciador.adicionarMidia(nova);
            atualizarListaView();
        }
    }

    //Ação do botão "Editar".
    // Abre o form com os dados da mídia selecionada e,
    // após a edição, atualiza no gerenciador.
     
    public void acaoEditar(Midia m) {
        Midia editada = viewPrincipal.pedirDadosEdicao(m);

        if (editada != null) {
            // a própria instância é modificada e devolvida pela view
            gerenciador.editarMidia(editada);
            atualizarListaView();
        }
    }
    
    //Ação do botão "Remover".
    //Remove a mídia da lista e do disco (arquivo .tpoo).
   
    public void acaoRemover(Midia m) {
        boolean ok = gerenciador.removerMidia(m);

        if (ok) {
            viewPrincipal.mostrarMensagem("Mídia removida com sucesso.");
            atualizarListaView();
        } else {
            viewPrincipal.mostrarMensagem("Falha ao remover mídia.");
        }
    }

    // Ação do botão "Mover".
    // Move o arquivo físico da mídia para outro diretório.
     
    public void acaoMover(Midia m, String novoCaminho) {
        boolean ok = gerenciador.moverMidia(m, novoCaminho);
        viewPrincipal.mostrarMensagem(ok ? "Movido com sucesso." : "Falha ao mover.");
        atualizarListaView();
    }

    // Ação do botão "Renomear".
    // Renomeia o arquivo físico da mídia.
     
    public void acaoRenomear(Midia m, String novoNome) {
        boolean ok = gerenciador.renomearMidia(m, novoNome);
        viewPrincipal.mostrarMensagem(ok ? "Renomeado com sucesso." : "Falha ao renomear.");
        atualizarListaView();
    }
    
    //Aplica filtros vindos da interface:
    // tipo (Filme, Livro, Música)
    // categoria
    // ordenação por título ou duração
    
    public void aplicarFiltro(Class<?> tipo, String categoria, String ordenacao) {
        List<Midia> resultado = gerenciador.filtrarCombinado(tipo, categoria);

        if ("titulo".equalsIgnoreCase(ordenacao)) {
            resultado = resultado.stream()
                    .sorted((a, b) -> a.getTitulo().compareToIgnoreCase(b.getTitulo()))
                    .toList();
        } else if ("duracao".equalsIgnoreCase(ordenacao)) {
            resultado = resultado.stream()
                    .sorted((a, b) -> Integer.compare(a.getDuracao(), b.getDuracao()))
                    .toList();
        }

        viewPrincipal.atualizarLista(resultado);
    }

    // Atualiza a lista da interface com todas as mídias salvas.
    
    public void atualizarListaView() {
        viewPrincipal.atualizarLista(gerenciador.getListaMidias());
    }
    
    // Permite trocar a view principal (opcional, conforme UML).
     
    public void setViewPrincipal(TelaPrincipal view) {
        this.viewPrincipal = view;
    }
}
