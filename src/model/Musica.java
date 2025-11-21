package model;

/**
 * Representa uma música no sistema. <br>
 * Esta classe herda de {@link Midia} e adiciona o atributo específico
 * referente ao artista responsável pela obra.
 *
 * <p>Uma música possui:</p>
 * <ul>
 *     <li>Informações gerais herdadas de {@link Midia}</li>
 *     <li>Nome do artista</li>
 * </ul>
 */
public class Musica extends Midia {

    /** Identificador de versão para serialização. */
    private static final long serialVersionUID = 1L;

    /** Nome do artista responsável pela música. */
    private String artista;

    /**
     * Construtor da classe {@code Musica}. <br>
     * Recebe todos os atributos necessários para inicializar uma mídia genérica,
     * além do nome do artista, que é exclusivo de músicas.
     *
     * @param caminhoArquivo caminho completo do arquivo de áudio
     * @param tamanhoEmDisco tamanho do arquivo em MB
     * @param titulo título da música
     * @param categoria categoria ou gênero (ex.: Pop, Rock, Jazz)
     * @param duracaoSegundos duração da música em segundos
     * @param artista nome do artista responsável pela obra
     */
    public Musica(String caminhoArquivo, double tamanhoEmDisco, String titulo,
                  String categoria, int duracaoSegundos, String artista) {

        super(caminhoArquivo, tamanhoEmDisco, titulo, categoria, duracaoSegundos);
        this.artista = artista;
    }

    /**
     * Retorna o nome do artista da música.
     *
     * @return nome do artista
     */
    public String getArtista() {
        return artista;
    }

    /**
     * Atualiza o nome do artista responsável pela música.
     *
     * @param artista novo nome do artista
     */
    public void setArtista(String artista) {
        this.artista = artista;
    }

    /**
     * Retorna uma string contendo apenas os atributos específicos
     * da classe {@code Musica}. <br>
     * Este método é utilizado pelo método {@link Midia#exibirDetalhes()}.
     *
     * @return texto descrevendo o artista da música
     */
    @Override
    public String exibirAtributosEspecificos() {
        return "Artista: " + artista;
    }
}
