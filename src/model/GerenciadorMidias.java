package model;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

//Classe responsável por gerenciar uma lista de mídias.
// Contém os métodos de adicionar, editar, remover, listar e persistir mídias.
// Esta classe representa a camada de negócio (model).
 
public class GerenciadorMidias {

    // Lista interna de mídias
    private List<Midia> listaMidias;

    // Diretório onde os arquivos .tpoo serão armazenados
    private String diretorioPersistencia;

    
     // Construtor que recebe o diretório onde as mídias serão persistidas.
     // Se o diretório não existir, ele é criado.
     
    public GerenciadorMidias(String diretorioPersistencia) {
        this.diretorioPersistencia = diretorioPersistencia;
        this.listaMidias = new ArrayList<>();

        File dir = new File(diretorioPersistencia);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    // Adiciona uma mídia ao sistema e salva seu arquivo .tpoo.
     
    public void adicionarMidia(Midia m) {
        listaMidias.add(m);
        salvarMidiaIndividual(m);
    }

   
     // Edita uma mídia já existente.
     // Atualiza seu arquivo .tpoo.
     
    public void editarMidia(Midia m) {
        salvarMidiaIndividual(m);
    }

    // Remove uma mídia da lista e exclui o arquivo .tpoo correspondente.
     
    public boolean removerMidia(Midia m) {
        boolean removido = listaMidias.remove(m);

        if (removido) {
            File f = new File(diretorioPersistencia, gerarNomeArquivoTPOO(m));
            if (f.exists()) {
                f.delete();
            }
        }
        return removido;
    }

    // Move o arquivo físico da mídia para outro diretório.
     
    public boolean moverMidia(Midia m, String novoCaminho) {
        File origem = new File(m.getCaminhoArquivo());
        File destino = new File(novoCaminho, origem.getName());

        boolean ok = origem.renameTo(destino);

        if (ok) {
            m.setCaminhoArquivo(destino.getAbsolutePath());
            salvarMidiaIndividual(m);
        }
        return ok;
    }

    // Renomeia o arquivo físico da mídia. Parecido com Mover, muda o destino
     
    public boolean renomearMidia(Midia m, String novoNome) {
        File origem = new File(m.getCaminhoArquivo());
        File destino = new File(origem.getParent(), novoNome);

        boolean ok = origem.renameTo(destino);

        if (ok) {
            m.setCaminhoArquivo(destino.getAbsolutePath());
            salvarMidiaIndividual(m);
        }
        return ok;
    }

    // Lista mídias por tipo: Filme, Música ou Livro. Dai usa a biblioteca Collectors
     
    public List<Midia> listarPorFormato(Class<?> tipo) {
        return listaMidias.stream()
                .filter(m -> tipo.isInstance(m))
                .collect(Collectors.toList());
    }

    
    // Lista mídias pela categoria informada. Também com collectors
    
    public List<Midia> listarPorCategoria(String categoria) {
        return listaMidias.stream()
                .filter(m -> m.getCategoria() != null &&
                             m.getCategoria().equalsIgnoreCase(categoria))
                .collect(Collectors.toList());
    }

    
     // Ordena as mídias por título em ordem alfabética.
     
    public void ordenarPorTitulo() {
        listaMidias.sort(Comparator.comparing(Midia::getTitulo, String.CASE_INSENSITIVE_ORDER));
    }

    
    //  Ordena as mídias por duração.
    // (minutos para filme, segundos para música, páginas para livro)
     
    public void ordenarPorDuracao() {
        listaMidias.sort(Comparator.comparingInt(Midia::getDuracao));
    }

    // Permite combinar formato + categoria em uma mesma filtragem.
    // Ex: todos os livros de aventura.
     
    public List<Midia> filtrarCombinado(Class<?> tipo, String categoria) {
        return listaMidias.stream()
                .filter(m -> tipo == null || tipo.isInstance(m))
                .filter(m -> categoria == null ||
                             categoria.isEmpty() ||
                            (m.getCategoria() != null &&
                             m.getCategoria().equalsIgnoreCase(categoria)))
                .collect(Collectors.toList());
    }

    // Salva todas as mídias do sistema, cada uma em seu .tpoo.
     
    public void salvarMidias() {
        for (Midia m : listaMidias) {
            salvarMidiaIndividual(m);
        }
    }

    
    // Carrega todas as mídias do diretório .tpoo para a lista interna.
     
    public void carregarMidias() {
        listaMidias.clear();

        File dir = new File(diretorioPersistencia);
        if (!dir.exists()) return;

        File[] arquivos = dir.listFiles((d, name) -> name.toLowerCase().endsWith(".tpoo"));
        if (arquivos == null) return;

        for (File f : arquivos) {
            Midia m = carregarMidiaDoArquivo(f);
            if (m != null) {
                listaMidias.add(m);
            }
        }
    }

    
    // Salva uma única mídia em formato .tpoo baseado no tipo.
     
     public void salvarMidiaIndividual(Midia m) {
        File out = new File(diretorioPersistencia, gerarNomeArquivoTPOO(m));

        try (PrintWriter pw = new PrintWriter(new FileWriter(out))) {

            if (m instanceof Filme f) {
                pw.println("TIPO = FILME");
                pw.println("IDIOMA = " + f.getIdiomaAudio());
            }
            else if (m instanceof Musica mu) {
                pw.println("TIPO = MUSICA");
                pw.println("ARTISTA = " + mu.getArtista());
            }
            else if (m instanceof Livro l) {
                pw.println("TIPO = LIVRO");
                pw.println("AUTORES = " + l.getAutores());
            }

            pw.println("TITULO = " + m.getTitulo());
            pw.println("CATEGORIA = " + m.getCategoria());
            pw.println("CAMINHO = " + m.getCaminhoArquivo());
            pw.println("TAMANHO = " + m.getTamanhoEmDisco());
            pw.println("DURACAO = " + m.getDuracao());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
     // Lê um arquivo .tpoo e recria o objeto Midia correspondente.
     
    public Midia carregarMidiaDoArquivo(File arquivo) {
        try {
            List<String> linhas = java.nio.file.Files.readAllLines(arquivo.toPath());
            Map<String, String> map = new HashMap<>();

            for (String l : linhas) {
                if (!l.contains("=")) continue;
                String[] p = l.split("=", 2);
                map.put(p[0].trim(), p[1].trim());
            }

            String tipo = map.get("TIPO");
            String caminho = map.get("CAMINHO");
            double tamanho = Double.parseDouble(map.get("TAMANHO"));
            String titulo = map.get("TITULO");
            String categoria = map.get("CATEGORIA");
            int duracao = Integer.parseInt(map.get("DURACAO"));

            if ("FILME".equals(tipo)) {
                return new Filme(caminho, tamanho, titulo, categoria, duracao,
                                 map.get("IDIOMA"));
            }
            else if ("MUSICA".equals(tipo)) {
                return new Musica(caminho, tamanho, titulo, categoria, duracao,
                                  map.get("ARTISTA"));
            }
            else if ("LIVRO".equals(tipo)) {
                return new Livro(caminho, tamanho, titulo, categoria, duracao,
                                 map.get("AUTORES"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    
     // Gera um nome seguro para o arquivo .tpoo baseado no título da mídia.
    
    private String gerarNomeArquivoTPOO(Midia m) {
        String tituloSafe = m.getTitulo() == null
                ? "midia"
                : m.getTitulo().replaceAll("[^a-zA-Z0-9\\-_\\.]", "_");

        return tituloSafe + "_" + Math.abs(m.hashCode()) + ".tpoo";
    }

    
    // Retorna a lista de mídias em modo somente leitura.
     
    public List<Midia> getListaMidias() {
        return Collections.unmodifiableList(listaMidias);
    }
}
