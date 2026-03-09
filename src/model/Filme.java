package model;

/**
 * Representa um filme dentro do sistema.
 * <p>
 * Esta classe herda de {@link Midia} e adiciona o atributo específico
 * relacionado ao idioma do áudio. Um objeto {@code Filme} possui todas as
 * características de uma mídia comum (título, categoria, duração etc.),
 * além do idioma em que o áudio do filme está disponível.
 */
public class Filme extends Midia {

    /** Identificador de versão para serialização. */
    private static final long serialVersionUID = 1L;

    /**
     * Idioma principal do áudio do filme.
     */
    private String idiomaAudio;

    /**
     * Construtor da classe Filme.
     * <p>
     * Inicializa um objeto contendo informações gerais da mídia,
     * herdadas de {@link Midia}, além do idioma específico do áudio.
     *
     * @param caminhoArquivo   caminho completo do arquivo de mídia no sistema
     * @param tamanhoEmDisco   tamanho do arquivo em disco, em megabytes
     * @param titulo           título do filme
     * @param categoria        categoria ou gênero do filme
     * @param duracaoMinutos   duração do filme em minutos
     * @param idiomaAudio      idioma do áudio da produção
     */
    public Filme(String caminhoArquivo, double tamanhoEmDisco, String titulo, String categoria,
                 int duracaoMinutos, String idiomaAudio) {

        super(caminhoArquivo, tamanhoEmDisco, titulo, categoria, duracaoMinutos);
        this.idiomaAudio = idiomaAudio;
    }

    /**
     * Retorna o idioma do áudio do filme.
     *
     * @return idioma do áudio
     */
    public String getIdiomaAudio() {
        return idiomaAudio;
    }

    /**
     * Define um novo idioma para o áudio do filme.
     *
     * @param idiomaAudio idioma a ser definido
     */
    public void setIdiomaAudio(String idiomaAudio) {
        this.idiomaAudio = idiomaAudio;
    }

    /**
     * Retorna uma string contendo os atributos específicos de um filme,
     * utilizada para exibição detalhada na interface.
     *
     * @return texto descrevendo o idioma do áudio
     */
    @Override
    public String exibirAtributosEspecificos() {
        return "Idioma do áudio: " + idiomaAudio;
    }
}
