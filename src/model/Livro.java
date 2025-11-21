package model;

/**
 * Representa um livro no sistema. <br>
 * Esta classe herda de {@link Midia} e adiciona o atributo específico
 * {@code autores}, contendo a lista de autores do livro.
 */
public class Livro extends Midia {

    /** Identificador de versão para serialização. */
    private static final long serialVersionUID = 1L;

    /** Lista de autores do livro (separados por vírgula). */
    private String autores;

    /**
     * Construtor da classe Livro. <br>
     * Recebe todos os dados necessários para criar um objeto livro, incluindo
     * informações herdadas de {@link Midia}.
     *
     * @param caminhoArquivo caminho completo onde o arquivo da mídia está armazenado
     * @param tamanhoEmDisco tamanho do arquivo em megabytes
     * @param titulo título do livro
     * @param categoria categoria da mídia (ex.: Romance, Terror, Ação)
     * @param paginas quantidade total de páginas (armazenada como duração)
     * @param autores autores do livro, separados por vírgula
     */
    public Livro(String caminhoArquivo, double tamanhoEmDisco, String titulo,
                 String categoria, int paginas, String autores) {
        super(caminhoArquivo, tamanhoEmDisco, titulo, categoria, paginas);
        this.autores = autores;
    }

    /**
     * Retorna a lista de autores do livro.
     *
     * @return string contendo os autores separados por vírgula
     */
    public String getAutores() {
        return autores;
    }

    /**
     * Atualiza a lista de autores do livro.
     *
     * @param autores nova lista de autores, separados por vírgula
     */
    public void setAutores(String autores) {
        this.autores = autores;
    }

    /**
     * Retorna uma string contendo apenas os atributos específicos de Livro.
     * <br>
     * Utilizado pelo método {@code exibirDetalhes()} na classe {@link Midia}.
     *
     * @return descrição textual dos atributos específicos
     */
    @Override
    public String exibirAtributosEspecificos() {
        return "Autores: " + autores;
    }
}
