package model;

import java.io.Serializable;

/**
 * Classe abstrata que representa uma mídia genérica no sistema. <br>
 * Todas as mídias específicas (como {@link Filme}, {@link Musica}, {@link Livro})
 * herdam desta classe. <br><br>
 *
 * Armazena informações comuns como:
 * <ul>
 *     <li>Caminho do arquivo</li>
 *     <li>Tamanho em disco</li>
 *     <li>Título</li>
 *     <li>Categoria</li>
 *     <li>Duração (minutos, segundos ou páginas dependendo da subclasse)</li>
 * </ul>
 *
 * Também fornece métodos utilitários para manipulação e exibição desses
 * atributos, além de implementar {@link Serializable} para permitir que
 * instâncias sejam persistidas em arquivos binários (.tpoo).
 */
public abstract class Midia implements Serializable {

    /** Identificador de versão para serialização. */
    private static final long serialVersionUID = 1L;

    /** Caminho completo do arquivo da mídia no sistema de arquivos. */
    private String caminhoArquivo;

    /** Tamanho do arquivo em disco (MB). */
    private double tamanhoEmDisco;

    /** Título da mídia. */
    private String titulo;

    /** Categoria ou gênero (ex.: Ação, Romance, Pop, Terror). */
    private String categoria;

    /**
     * Duração em uma unidade específica:
     * <ul>
     *     <li>Minutos para filmes</li>
     *     <li>Segundos para músicas</li>
     *     <li>Páginas para livros</li>
     * </ul>
     */
    private int duracao;

    /**
     * Construtor principal da classe Midia. <br>
     * Inicializa todos os atributos genéricos utilizados pelas subclasses.
     *
     * @param caminhoArquivo caminho físico do arquivo no sistema
     * @param tamanhoEmDisco tamanho do arquivo em MB
     * @param titulo título da mídia
     * @param categoria categoria ou gênero
     * @param duracao valor numérico representando a duração
     */
    public Midia(String caminhoArquivo, double tamanhoEmDisco, String titulo, String categoria, int duracao) {
        this.caminhoArquivo = caminhoArquivo;
        this.tamanhoEmDisco = tamanhoEmDisco;
        this.titulo = titulo;
        this.categoria = categoria;
        this.duracao = duracao;
    }

    /** @return caminho completo do arquivo da mídia */
    public String getCaminhoArquivo() {
        return caminhoArquivo;
    }

    /**
     * Define um novo caminho para o arquivo da mídia.
     *
     * @param caminhoArquivo novo caminho completo
     */
    public void setCaminhoArquivo(String caminhoArquivo) {
        this.caminhoArquivo = caminhoArquivo;
    }

    /** @return tamanho do arquivo em MB */
    public double getTamanhoEmDisco() {
        return tamanhoEmDisco;
    }

    /**
     * Atualiza o tamanho da mídia em disco.
     *
     * @param tamanhoEmDisco novo tamanho em MB
     */
    public void setTamanhoEmDisco(double tamanhoEmDisco) {
        this.tamanhoEmDisco = tamanhoEmDisco;
    }

    /** @return título da mídia */
    public String getTitulo() {
        return titulo;
    }

    /**
     * Define um novo título para a mídia.
     *
     * @param titulo novo título
     */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    /** @return categoria da mídia */
    public String getCategoria() {
        return categoria;
    }

    /**
     * Atualiza a categoria da mídia.
     *
     * @param categoria nova categoria ou gênero
     */
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    /** @return duração da mídia (min, seg ou páginas) */
    public int getDuracao() {
        return duracao;
    }

    /**
     * Define uma nova duração.
     *
     * @param duracao novo valor de duração
     */
    public void setDuracao(int duracao) {
        this.duracao = duracao;
    }

    /**
     * Retorna a extensão do arquivo da mídia, como "mp4", "mp3" ou "pdf". <br>
     * Caso o caminho não contenha extensão, retorna uma string vazia.
     *
     * @return extensão do arquivo em minúsculas
     */
    public String getExtensao() {
        if (caminhoArquivo == null) return "";
        int i = caminhoArquivo.lastIndexOf('.');
        if (i >= 0 && i < caminhoArquivo.length() - 1) {
            return caminhoArquivo.substring(i + 1).toLowerCase();
        }
        return "";
    }

    /**
     * Retorna uma representação textual básica da mídia,
     * contendo título, extensão e categoria.
     *
     * @return texto resumido da mídia
     */
    @Override
    public String toString() {
        return String.format("%s (%s) - %s", titulo, getExtensao(), categoria);
    }

    /**
     * Exibe todos os detalhes da mídia, incluindo:
     * <ul>
     *   <li>Atributos gerais (título, duração, etc.)</li>
     *   <li>Atributos específicos das subclasses</li>
     * </ul>
     *
     * @return string contendo todas as informações detalhadas da mídia
     */
    public String exibirDetalhes() {
        StringBuilder sb = new StringBuilder();
        sb.append("Título: ").append(titulo).append("\n");
        sb.append("Categoria: ").append(categoria).append("\n");
        sb.append("Duração: ").append(duracao).append("\n");
        sb.append("Caminho: ").append(caminhoArquivo).append("\n");
        sb.append("Tamanho (KB): ").append(tamanhoEmDisco).append("\n");

        sb.append(exibirAtributosEspecificos());

        return sb.toString();
    }

    /**
     * Método abstrato que deve ser implementado por cada subclasse,
     * retornando apenas os atributos específicos daquela mídia.
     *
     * @return string descrevendo os atributos particulares da subclasse
     */
    public abstract String exibirAtributosEspecificos();
}
