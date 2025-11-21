package test;

import model.*;
import org.junit.jupiter.api.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de testes unitários para a classe {@link GerenciadorMidias}. <br>
 * Utiliza JUnit 5 para validar o comportamento das operações de gerenciamento
 * de mídias como adicionar, remover, editar, ordenar, filtrar e manipular
 * arquivos fisicamente.
 *
 * <p>Para evitar efeitos colaterais no computador do usuário,
 * todos os testes utilizam pastas e arquivos temporários criados
 * automaticamente pelo JUnit.</p>
 */
public class GerenciadorMidiasTest {

    /** Instância do gerenciador utilizada em cada teste. */
    private GerenciadorMidias ger;

    /** Pasta temporária onde os arquivos fictícios são criados para os testes. */
    private File pastaTemp;

    /**
     * Configura o ambiente antes de cada teste. <br>
     * Cria uma pasta temporária e inicializa o {@link GerenciadorMidias}
     * apontando para ela.
     *
     * @throws Exception caso ocorra erro na criação da pasta temporária
     */
    @BeforeEach
    void setup() throws Exception {
        pastaTemp = Files.createTempDirectory("tpoo_test").toFile();
        ger = new GerenciadorMidias(pastaTemp.getAbsolutePath());
    }

    /**
     * Testa se o método {@code adicionarMidia()} adiciona corretamente uma mídia
     * à lista interna do gerenciador.
     */
    @Test
    void testAdicionarMidia() {
        Midia m = new Musica("caminho.mp3", 10, "Teste", "rock", 200, "Artista");
        ger.adicionarMidia(m);

        assertEquals(1, ger.getListaMidias().size());
        assertEquals("Teste", ger.getListaMidias().get(0).getTitulo());
    }

    /**
     * Testa se o método {@code removerMidia()} remove corretamente uma mídia
     * previamente adicionada.
     */
    @Test
    void testRemoverMidia() {
        Midia m = new Filme("filme.mp4", 20, "Filme A", "acao", 120, "Português");
        ger.adicionarMidia(m);

        boolean ok = ger.removerMidia(m);

        assertTrue(ok);
        assertEquals(0, ger.getListaMidias().size());
    }

    /**
     * Testa se o método {@code editarMidia()} registra mudanças feitas
     * diretamente no objeto da mídia.
     */
    @Test
    void testEditarMidia() {
        Midia m = new Livro("livro.pdf", 5, "Antigo", "romance", 300, "Autor");
        ger.adicionarMidia(m);

        m.setTitulo("Novo Nome");
        ger.editarMidia(m);

        assertEquals("Novo Nome", ger.getListaMidias().get(0).getTitulo());
    }

    /**
     * Testa o filtro por tipo de mídia usando o método {@code listarPorFormato()}.
     */
    @Test
    void testFiltrarPorTipo() {
        ger.adicionarMidia(new Filme("f.mp4", 20, "F", "acao", 100, "PT"));
        ger.adicionarMidia(new Musica("m.mp3", 5, "M", "rock", 180, "Artista"));

        List<Midia> filtro = ger.listarPorFormato(Musica.class);

        assertEquals(1, filtro.size());
        assertTrue(filtro.get(0) instanceof Musica);
    }

    /**
     * Testa o filtro por categoria através do método {@code listarPorCategoria()}.
     */
    @Test
    void testFiltrarPorCategoria() {
        ger.adicionarMidia(new Filme("f.mp4", 20, "F", "aventura", 100, "PT"));
        ger.adicionarMidia(new Livro("l.pdf", 5, "L", "romance", 200, "Autor"));

        List<Midia> filtro = ger.listarPorCategoria("aventura");

        assertEquals(1, filtro.size());
        assertEquals("F", filtro.get(0).getTitulo());
    }

    /**
     * Testa o filtro combinado por tipo e categoria
     * usando o método {@code filtrarCombinado()}.
     */
    @Test
    void testFiltrarCombinado() {
        ger.adicionarMidia(new Livro("l1.pdf", 5, "Livro", "aventura", 200, "Autor"));
        ger.adicionarMidia(new Filme("f1.mp4", 5, "Filme", "aventura", 200, "PT"));

        List<Midia> filtro = ger.filtrarCombinado(Livro.class, "aventura");

        assertEquals(1, filtro.size());
        assertEquals("Livro", filtro.get(0).getTitulo());
    }

    /**
     * Testa se o processo de salvar e carregar mídias via arquivo
     * funciona corretamente.
     */
    @Test
    void testSalvarECarregarMidias() {
        Midia m = new Musica("x.mp3", 10, "Titulo", "pop", 200, "Artista");
        ger.adicionarMidia(m);

        ger.salvarMidias();

        GerenciadorMidias ger2 = new GerenciadorMidias(pastaTemp.getAbsolutePath());
        ger2.carregarMidias();

        assertEquals(1, ger2.getListaMidias().size());
        assertEquals("Titulo", ger2.getListaMidias().get(0).getTitulo());
    }

    /**
     * Testa se {@link Midia#exibirDetalhes()} retorna corretamente
     * os detalhes específicos de um {@link Filme}.
     */
    @Test
    void testExibirDetalhesFilme() {
        Filme f = new Filme("c.mp4", 20, "Titulo", "acao", 120, "PT-BR");

        String s = f.exibirDetalhes();

        assertTrue(s.contains("PT-BR"));
        assertTrue(s.contains("120"));
    }

    /**
     * Testa a funcionalidade de mover fisicamente um arquivo de mídia
     * para outro diretório usando o método {@code moverMidia()}.
     *
     * @throws IOException caso ocorra erro na criação dos arquivos temporários
     */
    @Test
    void testMoverMidia() throws IOException {
        File arquivoOrigem = new File(pastaTemp, "c.mp4");
        Files.createFile(arquivoOrigem.toPath());

        Filme f = new Filme(arquivoOrigem.getAbsolutePath(), 20, "FilmeMover", "acao", 100, "PT");
        ger.adicionarMidia(f);

        File novaPasta = Files.createTempDirectory("nova").toFile();

        boolean ok = ger.moverMidia(f, novaPasta.getAbsolutePath());

        assertTrue(ok);
        assertTrue(f.getCaminhoArquivo().startsWith(novaPasta.getAbsolutePath()));
        assertTrue(new File(f.getCaminhoArquivo()).exists());
    }

    /**
     * Testa a funcionalidade de renomear um arquivo físico de mídia
     * utilizando o método {@code renomearMidia()}.
     */
    @Test
    void testRenomearMidia() throws IOException {
        File arquivoOrigem = new File(pastaTemp, "musica.mp3");
        Files.createFile(arquivoOrigem.toPath());

        Musica m = new Musica(arquivoOrigem.getAbsolutePath(), 5, "Antigo", "rock", 200, "Artista");
        ger.adicionarMidia(m);

        boolean ok = ger.renomearMidia(m, "NovoNome.mp3");

        assertTrue(ok);
        assertEquals("NovoNome.mp3", new File(m.getCaminhoArquivo()).getName());
        assertTrue(new File(m.getCaminhoArquivo()).exists());
    }

    /**
     * Testa as ordenações por título e duração usando os métodos
     * {@code ordenarPorTitulo()} e {@code ordenarPorDuracao()}.
     */
    @Test
    void testOrdenarPorTituloEDuracao() throws IOException {
        File arquivo1 = new File(pastaTemp, "l1.pdf");
        Files.createFile(arquivo1.toPath());

        File arquivo2 = new File(pastaTemp, "l2.pdf");
        Files.createFile(arquivo2.toPath());

        Livro l1 = new Livro(arquivo1.getAbsolutePath(), 5, "B", "aventura", 300, "Autor1");
        Livro l2 = new Livro(arquivo2.getAbsolutePath(), 5, "A", "aventura", 150, "Autor2");

        ger.adicionarMidia(l1);
        ger.adicionarMidia(l2);

        ger.ordenarPorTitulo();
        assertEquals("A", ger.getListaMidias().get(0).getTitulo());

        ger.ordenarPorDuracao();
        assertEquals(150, ger.getListaMidias().get(0).getDuracao());
    }

    /**
     * Testa se {@link Midia#exibirDetalhes()} inclui os atributos específicos
     * nas subclasses {@link Musica} e {@link Livro}.
     */
    @Test
    void testExibirDetalhesMusicaELivro() throws IOException {
        File arquivoMusica = new File(pastaTemp, "mus.mp3");
        Files.createFile(arquivoMusica.toPath());

        File arquivoLivro = new File(pastaTemp, "l.pdf");
        Files.createFile(arquivoLivro.toPath());

        Musica m = new Musica(arquivoMusica.getAbsolutePath(), 5, "M", "rock", 200, "Artista");
        Livro l = new Livro(arquivoLivro.getAbsolutePath(), 5, "L", "romance", 300, "Autor");

        assertTrue(m.exibirDetalhes().contains("Artista"));
        assertTrue(l.exibirDetalhes().contains("Autores"));
    }

}
