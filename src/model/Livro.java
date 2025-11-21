package model;


public class Livro extends Midia {

    // Lista de autores do livro (separados por vírgula)
    private String autores;

    
     //Construtor da classe Livro.
     // Recebe todas as informações necessárias para criar um livro.
    
    // caminhoArquivo → caminho onde o arquivo está armazenado
    // tamanhoEmDisco → tamanho do arquivo (MB)
     // titulo → título do livro
     // categoria → categoria (ex: Romance, Terror...)
     // paginas → número total de páginas
    // autores → autores do livro separados por vírgula
     
    public Livro(String caminhoArquivo, double tamanhoEmDisco, String titulo,
                 String categoria, int paginas, String autores) {
        super(caminhoArquivo, tamanhoEmDisco, titulo, categoria, paginas);
        this.autores = autores;
    }

    // Retorna os autores do livro
    public String getAutores() { return autores; }

    // Atualiza os autores do livro
    public void setAutores(String autores) { this.autores = autores; }

    
    // Retorna uma string contendo apenas os atributos específicos de Livro.
    // É usada pelo método exibirDetalhes() da classe Midia.
     
    @Override
    public String exibirAtributosEspecificos() {
        return "Autores: " + autores;
    }
}
