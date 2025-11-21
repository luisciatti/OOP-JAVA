package model;


 // Representa um filme. Herda de Midia.
 // Possui como atributo específico o idioma do áudio.
 
public class Filme extends Midia {
    
    private String idiomaAudio;

    
     //Construtor do filme.
     // Recebe caminho do arquivo, tamanho, título, categoria, duração em minutos e idioma do áudio.
     
    public Filme(String caminhoArquivo, double tamanhoEmDisco, String titulo, String categoria,
                 int duracaoMinutos, String idiomaAudio) {

        super(caminhoArquivo, tamanhoEmDisco, titulo, categoria, duracaoMinutos);
        this.idiomaAudio = idiomaAudio;
    }

    public String getIdiomaAudio() { 
        return idiomaAudio; 
    }

    public void setIdiomaAudio(String idiomaAudio) { 
        this.idiomaAudio = idiomaAudio; 
    }

    @Override
    public String exibirAtributosEspecificos() {
        return "Idioma do áudio: " + idiomaAudio;
    }
}
