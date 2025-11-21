package model;


public class Musica extends Midia {

   
	private static final long serialVersionUID = 1L;
	// Nome do artista responsável pela música
    private String artista;

    
     // Construtor da classe Musica.
     // Recebe os atributos gerais de mídia (caminho, tamanho, título, categoria, duração)
     // e também o artista, que é específico da música.
     
    public Musica(String caminhoArquivo, double tamanhoEmDisco, String titulo,
                  String categoria, int duracaoSegundos, String artista) {

        // Chama o construtor da classe mãe (Midia)
        super(caminhoArquivo, tamanhoEmDisco, titulo, categoria, duracaoSegundos);

        // Atribui o artista da música
        this.artista = artista;
    }

    // Retorna o nome do artista da música
    public String getArtista() {
        return artista;
    }

    // Atualiza o nome do artista da música
    public void setArtista(String artista) {
        this.artista = artista;
    }

    /*
     * Retorna uma String exibindo apenas os atributos específicos da música,
     * conforme exigido na implementação de Midia.
     */
    @Override
    public String exibirAtributosEspecificos() {
        return "Artista: " + artista;
    }
}
