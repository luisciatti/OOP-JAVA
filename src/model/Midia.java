package model;

import java.io.Serializable;


// Classe abstrata que representa uma mídia genérica.
// Todas as mídias (Filme, Música, Livro) herdam desta classe.
//
 //Possui atributos básicos como caminho do arquivo, tamanho em disco,
 //título, categoria e duração. Também disponibiliza métodos para
 // manipulação e exibição dessas informações.
 
 //Implementa Serializable para permitir persistência em arquivos .tpoo(O negocio dificil).
 
public abstract class Midia implements Serializable {

    private static final long serialVersionUID = 1L;

    // Caminho completo do arquivo de mídia no sistema de arquivos
    private String caminhoArquivo;

    // Tamanho do arquivo em disco
    private double tamanhoEmDisco;

    // Título da mídia
    private String titulo;

    // Categoria ou gênero (ex: ação, romance, pop, etc.)
    private String categoria;

    // Duração em unidades específicas: minutos (filme),
    // segundos (música) ou páginas (livro)
    private int duracao;

    
     // Construtor principal da classe Midia.
     // Inicializa todos os atributos genéricos de uma mídia.
     
    public Midia(String caminhoArquivo, double tamanhoEmDisco, String titulo, String categoria, int duracao) {
        this.caminhoArquivo = caminhoArquivo;
        this.tamanhoEmDisco = tamanhoEmDisco;
        this.titulo = titulo;
        this.categoria = categoria;
        this.duracao = duracao;
    }

    // Retorna o caminho do arquivo
    public String getCaminhoArquivo() { 
        return caminhoArquivo; 
    }

    // Define um novo caminho de arquivo
    public void setCaminhoArquivo(String caminhoArquivo) { 
        this.caminhoArquivo = caminhoArquivo; 
    }

    // Retorna o tamanho em disco
    public double getTamanhoEmDisco() { 
        return tamanhoEmDisco; 
    }

    // Define um novo tamanho em disco
    public void setTamanhoEmDisco(double tamanhoEmDisco) { 
        this.tamanhoEmDisco = tamanhoEmDisco; 
    }

    // Retorna o título
    public String getTitulo() { 
        return titulo; 
    }

    // Define um novo título
    public void setTitulo(String titulo) { 
        this.titulo = titulo; 
    }

    // Retorna a categoria
    public String getCategoria() { 
        return categoria; 
    }

    // Define uma nova categoria
    public void setCategoria(String categoria) { 
        this.categoria = categoria; 
    }

    // Retorna a duração
    public int getDuracao() { 
        return duracao; 
    }

    // Define uma nova duração
    public void setDuracao(int duracao) { 
        this.duracao = duracao; 
    }

    
     // Retorna a extensão do arquivo da mídia.
     // Exemplo: "mp4", "mp3", "pdf".
     
    public String getExtensao() {
        if (caminhoArquivo == null) return "";
        int i = caminhoArquivo.lastIndexOf('.');
        if (i >= 0 && i < caminhoArquivo.length() - 1) {
            return caminhoArquivo.substring(i + 1).toLowerCase();
        }
        return "";
    }

    
     // Retorna uma representação textual básica da mídia,
     //contendo título, extensão e categoria.
     
    @Override
    public String toString() {
        return String.format("%s (%s) - %s", titulo, getExtensao(), categoria);
    }

    
     // Exibe todos os detalhes da mídia, incluindo
     //atributos gerais e específicos (dependendo da subclasse).
     
    public String exibirDetalhes() {
        StringBuilder sb = new StringBuilder();
        sb.append("Título: ").append(titulo).append("\n");
        sb.append("Categoria: ").append(categoria).append("\n");
        sb.append("Duração: ").append(duracao).append("\n");
        sb.append("Caminho: ").append(caminhoArquivo).append("\n");
        sb.append("Tamanho (KB): ").append(tamanhoEmDisco).append("\n");

        // adiciona os atributos da subclasse
        sb.append(exibirAtributosEspecificos());

        return sb.toString();
    }

    
     // Método abstrato que deve ser implementado pelas subclasses
      //  para exibir apenas os atributos específicos. para fazer o 10 lá
   
     
    public abstract String exibirAtributosEspecificos();
}
