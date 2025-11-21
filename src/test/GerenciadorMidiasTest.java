package test;

import model.*;
import org.junit.jupiter.api.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GerenciadorMidiasTest {

    private GerenciadorMidias ger;
    private File pastaTemp;

    @BeforeEach
    void setup() throws Exception {
        // Cria uma pasta temporária para os testes salvarem arquivos sem mexer no PC
        pastaTemp = Files.createTempDirectory("tpoo_test").toFile();

        // Instancia o Gerenciador usando essa pasta temporária
        ger = new GerenciadorMidias(pastaTemp.getAbsolutePath());
    }

    @Test
    void testAdicionarMidia() {
        // Cria uma mídia simples
        Midia m = new Musica("caminho.mp3", 10, "Teste", "rock", 200, "Artista");

        // Adiciona ao gerenciador
        ger.adicionarMidia(m);

        // Verifica se realmente foi adicionada
        assertEquals(1, ger.getListaMidias().size());
        assertEquals("Teste", ger.getListaMidias().get(0).getTitulo());
    }

    @Test
    void testRemoverMidia() {
        // Cria e adiciona uma mídia
        Midia m = new Filme("filme.mp4", 20, "Filme A", "acao", 120, "Português");
        ger.adicionarMidia(m);

        // Remove do gerenciador
        boolean ok = ger.removerMidia(m);

        // Verifica se removeu com sucesso
        assertTrue(ok);
        assertEquals(0, ger.getListaMidias().size());
    }

    @Test
    void testEditarMidia() {
        // Adiciona um livro
        Midia m = new Livro("livro.pdf", 5, "Antigo", "romance", 300, "Autor");
        ger.adicionarMidia(m);

        // Altera o título na própria instância
        m.setTitulo("Novo Nome");

        // Confirma que o método do gerenciador registra essa mudança
        ger.editarMidia(m);

        // Verifica se a edição realmente foi aplicada
        assertEquals("Novo Nome", ger.getListaMidias().get(0).getTitulo());
    }

    @Test
    void testFiltrarPorTipo() {
        // Adiciona mídias diferentes
        ger.adicionarMidia(new Filme("f.mp4", 20, "F", "acao", 100, "PT"));
        ger.adicionarMidia(new Musica("m.mp3", 5, "M", "rock", 180, "Artista"));

        // Filtra apenas músicas
        List<Midia> filtro = ger.listarPorFormato(Musica.class);

        // Deve retornar só 1 (a música)
        assertEquals(1, filtro.size());
        assertTrue(filtro.get(0) instanceof Musica);
    }

    @Test
    void testFiltrarPorCategoria() {
        // Adiciona mídias com categorias diferentes
        ger.adicionarMidia(new Filme("f.mp4", 20, "F", "aventura", 100, "PT"));
        ger.adicionarMidia(new Livro("l.pdf", 5, "L", "romance", 200, "Autor"));

        // Filtra pela categoria "aventura"
        List<Midia> filtro = ger.listarPorCategoria("aventura");

        // Deve retornar apenas o filme
        assertEquals(1, filtro.size());
        assertEquals("F", filtro.get(0).getTitulo());
    }

    @Test
    void testFiltrarCombinado() {
        // Adiciona um livro e um filme com mesma categoria
        ger.adicionarMidia(new Livro("l1.pdf", 5, "Livro", "aventura", 200, "Autor"));
        ger.adicionarMidia(new Filme("f1.mp4", 5, "Filme", "aventura", 200, "PT"));

        // Filtra apenas Livros da categoria "aventura"
        List<Midia> filtro = ger.filtrarCombinado(Livro.class, "aventura");

        // Deve retornar só o livro
        assertEquals(1, filtro.size());
        assertEquals("Livro", filtro.get(0).getTitulo());
    }

    @Test
    void testSalvarECarregarMidias() {
        // Adiciona uma mídia
        Midia m = new Musica("x.mp3", 10, "Titulo", "pop", 200, "Artista");
        ger.adicionarMidia(m);

        // Salva no arquivo da pasta temporária
        ger.salvarMidias();

        // Cria outro gerenciador apontando para mesma pasta
        GerenciadorMidias ger2 = new GerenciadorMidias(pastaTemp.getAbsolutePath());

        // Carrega do arquivo
        ger2.carregarMidias();

        // Verifica se a mídia carregada é a mesma que foi salva
        assertEquals(1, ger2.getListaMidias().size());
        assertEquals("Titulo", ger2.getListaMidias().get(0).getTitulo());
    }

    @Test
    void testExibirDetalhesFilme() {
        // Cria um filme com atributos conhecidos
        Filme f = new Filme("c.mp4", 20, "Titulo", "acao", 120, "PT-BR");

        // Pega o texto dos detalhes
        String s = f.exibirDetalhes();

        // Verifica se contém as informações específicas
        assertTrue(s.contains("PT-BR"));
        assertTrue(s.contains("120"));
    }
    @Test
    void testMoverMidia() throws IOException {
        // Cria arquivo físico temporário
        File arquivoOrigem = new File(pastaTemp, "c.mp4");
        Files.createFile(arquivoOrigem.toPath());

        Filme f = new Filme(arquivoOrigem.getAbsolutePath(), 20, "FilmeMover", "acao", 100, "PT");
        ger.adicionarMidia(f);

        // Cria nova pasta temporária
        File novaPasta = Files.createTempDirectory("nova").toFile();

        // Move a mídia
        boolean ok = ger.moverMidia(f, novaPasta.getAbsolutePath());

        assertTrue(ok, "A mídia não foi movida corretamente");
        assertTrue(f.getCaminhoArquivo().startsWith(novaPasta.getAbsolutePath()), "Caminho da mídia não atualizado");
        assertTrue(new File(f.getCaminhoArquivo()).exists(), "Arquivo não existe na nova pasta");
    }

    @Test
    void testRenomearMidia() throws IOException {
        // Cria arquivo físico temporário
        File arquivoOrigem = new File(pastaTemp, "musica.mp3");
        Files.createFile(arquivoOrigem.toPath());

        Musica m = new Musica(arquivoOrigem.getAbsolutePath(), 5, "Antigo", "rock", 200, "Artista");
        ger.adicionarMidia(m);

        // Renomeia o arquivo
        boolean ok = ger.renomearMidia(m, "NovoNome.mp3");

        assertTrue(ok, "A mídia não foi renomeada corretamente");
        assertEquals("NovoNome.mp3", new File(m.getCaminhoArquivo()).getName(), "Nome do arquivo não atualizado");
        assertTrue(new File(m.getCaminhoArquivo()).exists(), "Arquivo renomeado não existe fisicamente");
    }

    @Test
    void testOrdenarPorTituloEDuracao() throws IOException {
        // Cria arquivos temporários reais
        File arquivo1 = new File(pastaTemp, "l1.pdf");
        Files.createFile(arquivo1.toPath());

        File arquivo2 = new File(pastaTemp, "l2.pdf");
        Files.createFile(arquivo2.toPath());

        // Cria objetos Livro com caminhos reais
        Livro l1 = new Livro(arquivo1.getAbsolutePath(), 5, "B", "aventura", 300, "Autor1");
        Livro l2 = new Livro(arquivo2.getAbsolutePath(), 5, "A", "aventura", 150, "Autor2");

        ger.adicionarMidia(l1);
        ger.adicionarMidia(l2);

        // Ordena por título
        ger.ordenarPorTitulo();
        assertEquals("A", ger.getListaMidias().get(0).getTitulo(), "Ordenação por título incorreta");

        // Ordena por duração
        ger.ordenarPorDuracao();
        assertEquals(150, ger.getListaMidias().get(0).getDuracao(), "Ordenação por duração incorreta");
    }


    @Test
    void testExibirDetalhesMusicaELivro() throws IOException {
        // Cria arquivos temporários para Musica e Livro
        File arquivoMusica = new File(pastaTemp, "mus.mp3");
        Files.createFile(arquivoMusica.toPath());

        File arquivoLivro = new File(pastaTemp, "l.pdf");
        Files.createFile(arquivoLivro.toPath());

        // Cria os objetos com os caminhos dos arquivos reais
        Musica m = new Musica(arquivoMusica.getAbsolutePath(), 5, "M", "rock", 200, "Artista");
        Livro l = new Livro(arquivoLivro.getAbsolutePath(), 5, "L", "romance", 300, "Autor");

        // Verifica se exibirDetalhes contém os atributos específicos
        assertTrue(m.exibirDetalhes().contains("Artista"), "Detalhes da música não contêm o artista");
        assertTrue(l.exibirDetalhes().contains("Autores"), "Detalhes do livro não contêm os autores");
    }



}
