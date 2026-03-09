package controller;

import model.GerenciadorMidias;
import model.Midia;
import view.TelaPrincipal;

import java.util.List;

/**
 * Controlador responsável por realizar a comunicação entre a camada de visão (view)
 * e o {@link GerenciadorMidias}, que contém as regras de negócio do sistema.
 * <p>
 * Esta classe centraliza ações de adicionar, editar, remover, mover e renomear mídias,
 * bem como aplicar filtros e atualizar a lista exibida na interface gráfica.
 */
public class MidiaController {

    /**
     * Instância do gerenciador que concentra todas as regras de negócio
     * relacionadas às mídias.
     */
    private GerenciadorMidias gerenciador;

    /**
     * Janela principal da interface gráfica da aplicação.
     */
    private TelaPrincipal viewPrincipal;

    /**
     * Construtor da classe. Recebe a view principal e o gerenciador de mídias.
     *
     * @param gerenciador objeto responsável pelas regras de negócio
     * @param view        interface gráfica principal
     */
    public MidiaController(GerenciadorMidias gerenciador, TelaPrincipal view) {
        this.gerenciador = gerenciador;
        this.viewPrincipal = view;
    }

    /**
     * Inicializa o sistema realizando:
     * <ul>
     *     <li>Carregamento das mídias salvas no diretório;</li>
     *     <li>Atualização da lista na interface gráfica;</li>
     *     <li>Exibição da tela principal.</li>
     * </ul>
     */
    public void inicializar() {
        gerenciador.carregarMidias();
        atualizarListaView();
        viewPrincipal.mostrar();
    }

    /**
     * Ação executada quando o usuário clica no botão "Adicionar".
     * <p>
     * Abre o formulário de edição/criação e, caso preenchido corretamente,
     * adiciona a nova mídia ao gerenciador.
     */
    public void acaoAdicionar() {
        Midia nova = viewPrincipal.pedirDadosEdicao(null);

        if (nova != null) {
            gerenciador.adicionarMidia(nova);
            atualizarListaView();
        }
    }

    /**
     * Ação executada quando o usuário clica no botão "Editar".
     * <p>
     * Abre o formulário com os dados da mídia selecionada e, caso editada,
     * atualiza a instância existente no gerenciador.
     *
     * @param m mídia selecionada para edição
     */
    public void acaoEditar(Midia m) {
        Midia editada = viewPrincipal.pedirDadosEdicao(m);

        if (editada != null) {
            gerenciador.editarMidia(editada);
            atualizarListaView();
        }
    }

    /**
     * Ação do botão "Remover".
     * <p>
     * Remove a mídia tanto da lista quanto do disco (arquivo .tpoo).
     *
     * @param m mídia a ser removida
     */
    public void acaoRemover(Midia m) {
        boolean ok = gerenciador.removerMidia(m);

        if (ok) {
            viewPrincipal.mostrarMensagem("Mídia removida com sucesso.");
            atualizarListaView();
        } else {
            viewPrincipal.mostrarMensagem("Falha ao remover mídia.");
        }
    }

    /**
     * Ação do botão "Mover".
     * <p>
     * Move o arquivo físico da mídia para um novo diretório.
     *
     * @param m           mídia a ser movida
     * @param novoCaminho caminho de destino
     */
    public void acaoMover(Midia m, String novoCaminho) {
        boolean ok = gerenciador.moverMidia(m, novoCaminho);
        viewPrincipal.mostrarMensagem(ok ? "Movido com sucesso." : "Falha ao mover.");
        atualizarListaView();
    }

    /**
     * Ação do botão "Renomear".
     * <p>
     * Renomeia o arquivo físico associado à mídia.
     *
     * @param m        mídia a ser renomeada
     * @param novoNome novo nome do arquivo
     */
    public void acaoRenomear(Midia m, String novoNome) {
        boolean ok = gerenciador.renomearMidia(m, novoNome);
        viewPrincipal.mostrarMensagem(ok ? "Renomeado com sucesso." : "Falha ao renomear.");
        atualizarListaView();
    }

    /**
     * Aplica filtros recebidos pela interface gráfica.
     * <p>
     * Os filtros podem incluir:
     * <ul>
     *     <li>Tipo da mídia (Filme, Livro, Música);</li>
     *     <li>Categoria;</li>
     *     <li>Ordenação por título ou duração.</li>
     * </ul>
     *
     * @param tipo       tipo de mídia (classe específica) ou {@code null} para não filtrar pelo tipo
     * @param categoria  categoria desejada ou {@code null} para não filtrar por categoria
     * @param ordenacao  tipo de ordenação ("titulo" ou "duracao")
     */
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

    /**
     * Atualiza a lista apresentada na interface com todas as mídias atualmente
     * salvas no gerenciador.
     */
    public void atualizarListaView() {
        viewPrincipal.atualizarLista(gerenciador.getListaMidias());
    }

    /**
     * Permite alterar a view principal, caso necessário.
     *
     * @param view nova interface principal
     */
    public void setViewPrincipal(TelaPrincipal view) {
        this.viewPrincipal = view;
    }
}
