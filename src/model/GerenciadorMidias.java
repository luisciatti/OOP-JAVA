package model;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Classe responsável por gerenciar todas as operações relacionadas às mídias do sistema.
 * <p>
 * Esta classe representa a camada de negócio (model) e mantém uma lista interna de objetos
 * {@link Midia}, fornecendo funcionalidades como:
 * <ul>
 *     <li>Adicionar, editar e remover mídias</li>
 *     <li>Listar e filtrar mídias por tipo ou categoria</li>
 *     <li>Ordenar mídias por título ou duração</li>
 *     <li>Persistir mídias em arquivos individuais no formato <code>.tpoo</code></li>
 *     <li>Carregar mídias salvas previamente</li>
 *     <li>Mover e renomear arquivos físicos associados à mídia</li>
 * </ul>
 */
public class GerenciadorMidias {

    /** Lista interna que armazena todas as mídias do sistema. */
    private List<Midia> listaMidias;

    /** Diretório onde os arquivos de persistência (.tpoo) serão gravados. */
    private String diretorioPersistencia;

    /**
     * Construtor do gerenciador.
     * <p>
     * O diretório definido para armazenar os arquivos .tpoo é verificado e, caso
     * não exista, é automaticamente criado.
     *
     * @param diretorioPersistencia caminho do diretório onde os arquivos serão salvos
     */
    public GerenciadorMidias(String diretorioPersistencia) {
        this.diretorioPersistencia = diretorioPersistencia;
        this.listaMidias = new ArrayList<>();

        File dir = new File(diretorioPersistencia);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    /**
     * Adiciona uma mídia à lista interna e salva sua representação em um arquivo .tpoo.
     *
     * @param m mídia a ser adicionada
     */
    public void adicionarMidia(Midia m) {
        listaMidias.add(m);
        salvarMidiaIndividual(m);
    }

    /**
     * Atualiza os dados de uma mídia já existente e persiste as alterações no arquivo .tpoo.
     *
     * @param m mídia que foi editada
     */
    public void editarMidia(Midia m) {
        salvarMidiaIndividual(m);
    }

    /**
     * Remove uma mídia do sistema e apaga seu arquivo .tpoo correspondente.
     *
     * @param m mídia a ser removida
     * @return {@code true} se a mídia foi removida com sucesso;
     *         {@code false} caso contrário
     */
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

    /**
     * Move o arquivo físico da mídia para um novo diretório.
     * <p>
     * Caso a operação tenha sucesso, o caminho interno da mídia é atualizado
     * e o arquivo .tpoo também é reescrito.
     *
     * @param m mídia cujo arquivo será movido
     * @param novoCaminho caminho do novo diretório destino
     * @return {@code true} se o arquivo foi movido; {@code false} caso contrário
     */
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

    /**
     * Renomeia o arquivo físico associado à mídia.
     *
     * @param m mídia cujo arquivo será renomeado
     * @param novoNome novo nome do arquivo (sem alterar diretório)
     * @return {@code true} se renomeado com sucesso; {@code false} caso contrário
     */
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

    /**
     * Retorna todas as mídias do tipo especificado (Filme, Música ou Livro).
     *
     * @param tipo classe correspondente ao tipo desejado
     * @return lista contendo todas as mídias do tipo informado
     */
    public List<Midia> listarPorFormato(Class<?> tipo) {
        return listaMidias.stream()
                .filter(m -> tipo.isInstance(m))
                .collect(Collectors.toList());
    }

    /**
     * Lista todas as mídias pertencentes a uma categoria específica.
     *
     * @param categoria categoria desejada (ex.: "Ação", "Romance")
     * @return lista de mídias da categoria informada
     */
    public List<Midia> listarPorCategoria(String categoria) {
        return listaMidias.stream()
                .filter(m -> m.getCategoria() != null &&
                             m.getCategoria().equalsIgnoreCase(categoria))
                .collect(Collectors.toList());
    }

    /**
     * Ordena a lista interna de mídias por título, ignorando diferenças entre maiúsculas e minúsculas.
     */
    public void ordenarPorTitulo() {
        listaMidias.sort(Comparator.comparing(Midia::getTitulo, String.CASE_INSENSITIVE_ORDER));
    }

    /**
     * Ordena a lista interna de mídias pela duração.
     * <p>
     * Duração é interpretada conforme o tipo:
     * <ul>
     *     <li>Filme: minutos</li>
     *     <li>Música: segundos</li>
     *     <li>Livro: número de páginas</li>
     * </ul>
     */
    public void ordenarPorDuracao() {
        listaMidias.sort(Comparator.comparingInt(Midia::getDuracao));
    }

    /**
     * Aplica filtro combinado por formato e categoria.
     *
     * @param tipo classe usada como filtro de tipo (ou {@code null} para ignorar)
     * @param categoria categoria desejada (ou {@code null} / vazio para ignorar)
     * @return lista filtrada conforme critérios informados
     */
    public List<Midia> filtrarCombinado(Class<?> tipo, String categoria) {
        return listaMidias.stream()
                .filter(m -> tipo == null || tipo.isInstance(m))
                .filter(m -> categoria == null ||
                             categoria.isEmpty() ||
                            (m.getCategoria() != null &&
                             m.getCategoria().equalsIgnoreCase(categoria)))
                .collect(Collectors.toList());
    }

    /**
     * Salva todas as mídias do sistema, gerando um arquivo .tpoo para cada uma.
     */
    public void salvarMidias() {
        for (Midia m : listaMidias) {
            salvarMidiaIndividual(m);
        }
    }

    /**
     * Carrega todas as mídias salvas no diretório de persistência,
     * recriando os objetos correspondentes a partir dos arquivos .tpoo.
     */
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

    /**
     * Salva uma única mídia no formato .tpoo.
     * <p>
     * O conteúdo salvo varia conforme o tipo da mídia (Filme, Música ou Livro).
     *
     * @param m mídia a ser salva
     */
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

    /**
     * Lê um arquivo .tpoo e reconstrói o objeto {@link Midia} correspondente,
     * identificando o tipo a partir do campo "TIPO".
     *
     * @param arquivo arquivo .tpoo contendo os dados serializados
     * @return objeto Midia reconstruído ou {@code null} se ocorrer erro
     */
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

    /**
     * Gera automaticamente um nome seguro para o arquivo de persistência .tpoo,
     * baseado no título da mídia e em seu hash code.
     *
     * @param m mídia usada para gerar o nome
     * @return nome seguro do arquivo no formato <code>titulo_hash.tpoo</code>
     */
    private String gerarNomeArquivoTPOO(Midia m) {
        String tituloSafe = m.getTitulo() == null
                ? "midia"
                : m.getTitulo().replaceAll("[^a-zA-Z0-9\\-_\\.]", "_");

        return tituloSafe + "_" + Math.abs(m.hashCode()) + ".tpoo";
    }

    /**
     * Retorna uma cópia somente leitura da lista de mídias cadastradas.
     *
     * @return lista imutável de mídias
     */
    public List<Midia> getListaMidias() {
        return Collections.unmodifiableList(listaMidias);
    }
}
