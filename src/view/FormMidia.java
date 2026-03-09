package view;

import model.Filme;
import model.Livro;
import model.Midia;
import model.Musica;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * Janela modal (JDialog) responsável por criar ou editar uma instância de {@link Midia}.
 * <p>
 * O comportamento depende do parâmetro {@code mFornecida} enviado ao método {@link #mostrar(Midia)}:
 * <ul>
 *     <li><b>Modo criação</b>: quando {@code mFornecida == null}. O usuário escolhe um arquivo e preenche os dados para gerar uma nova mídia.</li>
 *     <li><b>Modo edição</b>: quando {@code mFornecida != null}. Os dados são carregados no formulário, e os valores alterados atualizam a própria instância.</li>
 * </ul>
 * Esta classe suporta criação/edição de:
 * <ul>
 *     <li>{@link Filme}</li>
 *     <li>{@link Musica}</li>
 *     <li>{@link Livro}</li>
 * </ul>
 */
public class FormMidia extends JDialog {

    private static final long serialVersionUID = 1L;

    /**
     * Tipo de mídia a ser criada (Filme.class, Musica.class ou Livro.class).
     * Caso seja {@code null}, o tipo será perguntado ao usuário.
     */
    private Class<?> tipo;

    /**
     * Construtor padrão do formulário.
     *
     * @param parent janela principal (Frame) que abrirá este JDialog
     * @param tipo   classe da mídia que será criada; pode ser {@code null} para perguntar ao usuário
     */
    public FormMidia(Frame parent, Class<?> tipo) {
        super(parent, true);
        this.tipo = tipo;
        setTitle("Formulário de Mídia");
        setSize(400, 300);
        setLocationRelativeTo(parent);
    }

    /**
     * Exibe o formulário para criação ou edição de uma mídia.
     *
     * <h3>Comportamento</h3>
     *
     * <ul>
     *     <li><b>Modo criação:</b> quando {@code mFornecida == null}. O usuário escolhe o arquivo e preenche os dados; retorna uma nova instância.</li>
     *     <li><b>Modo edição:</b> quando {@code mFornecida != null}. Os valores atuais são pré-carregados e, ao confirmar, alteram a instância existente.</li>
     * </ul>
     *
     * @param mFornecida mídia existente a ser editada, ou {@code null} para criar uma nova
     * @return objeto {@link Midia} criado/alterado ou {@code null} se o usuário cancelar
     */
    public Midia mostrar(Midia mFornecida) {

        // MODO CRIAÇÃO — Seleciona arquivo
        File f = null;
        if (mFornecida == null) {
            JFileChooser chooser = new JFileChooser();
            int res = chooser.showOpenDialog(this);
            if (res != JFileChooser.APPROVE_OPTION) return null;
            f = chooser.getSelectedFile();
        }

        // Valores iniciais para edição
        String tituloInit = mFornecida != null ? mFornecida.getTitulo() : "";
        String categoriaInit = mFornecida != null ? mFornecida.getCategoria() : "";
        String duracaoInit = mFornecida != null ? String.valueOf(mFornecida.getDuracao()) : "";

        // Entrada de Título
        String titulo = (String) JOptionPane.showInputDialog(
                this, "Título:", "Título",
                JOptionPane.PLAIN_MESSAGE, null, null, tituloInit
        );
        if (titulo == null) return null;

        // Entrada de Categoria
        String categoria = (String) JOptionPane.showInputDialog(
                this, "Categoria:", "Categoria",
                JOptionPane.PLAIN_MESSAGE, null, null, categoriaInit
        );
        if (categoria == null) return null;

        // Entrada de Duração
        String duracaoStr = (String) JOptionPane.showInputDialog(
                this, "Duração (int - minutos/segundos/páginas):",
                "Duração", JOptionPane.PLAIN_MESSAGE, null, null, duracaoInit
        );
        if (duracaoStr == null) return null;

        int duracao;
        try {
            duracao = Integer.parseInt(duracaoStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Duração inválida.", "Erro", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        // Determina o tipo da mídia
        Class<?> finalTipo = tipo;
        if (finalTipo == null) {
            String[] op = {"Filme", "Musica", "Livro"};
            int escolha = JOptionPane.showOptionDialog(
                    this, "Tipo de mídia", "Escolha",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                    null, op, op[0]
            );

            if (escolha == 0) finalTipo = Filme.class;
            else if (escolha == 1) finalTipo = Musica.class;
            else if (escolha == 2) finalTipo = Livro.class;
            else return null;
        }

        double tamanho;
        String caminhoArquivo;

        // MODO EDIÇÃO — Mantém caminho/tamanho
        if (mFornecida != null) {
            caminhoArquivo = mFornecida.getCaminhoArquivo();
            tamanho = mFornecida.getTamanhoEmDisco();
        } else {
            tamanho = f.length() / 1024.0;
            caminhoArquivo = f.getAbsolutePath();
        }

        // Modo edição: atualiza instância existente
        if (mFornecida != null) {
            mFornecida.setTitulo(titulo);
            mFornecida.setCategoria(categoria);
            mFornecida.setDuracao(duracao);

            // Atualiza atributos específicos
            if (mFornecida instanceof Filme) {
                String idiomaInit = ((Filme) mFornecida).getIdiomaAudio();
                String idioma = (String) JOptionPane.showInputDialog(
                        this, "Idioma do áudio:", "Idioma",
                        JOptionPane.PLAIN_MESSAGE, null, null, idiomaInit
                );
                if (idioma == null) return null;
                ((Filme) mFornecida).setIdiomaAudio(idioma);

            } else if (mFornecida instanceof Musica) {
                String artistaInit = ((Musica) mFornecida).getArtista();
                String artista = (String) JOptionPane.showInputDialog(
                        this, "Artista:", "Artista",
                        JOptionPane.PLAIN_MESSAGE, null, null, artistaInit
                );
                if (artista == null) return null;
                ((Musica) mFornecida).setArtista(artista);

            } else if (mFornecida instanceof Livro) {
                String autoresInit = ((Livro) mFornecida).getAutores();
                String autores = (String) JOptionPane.showInputDialog(
                        this, "Autores (separe por vírgula):", "Autores",
                        JOptionPane.PLAIN_MESSAGE, null, null, autoresInit
                );
                if (autores == null) return null;
                ((Livro) mFornecida).setAutores(autores);
            }

            return mFornecida;
        }

        // Modo criação: instancia novo objeto conforme tipo
        if (finalTipo == Filme.class) {
            String idioma = JOptionPane.showInputDialog(this, "Idioma do áudio:");
            if (idioma == null) return null;
            return new Filme(caminhoArquivo, tamanho, titulo, categoria, duracao, idioma);

        } else if (finalTipo == Musica.class) {
            String artista = JOptionPane.showInputDialog(this, "Artista:");
            if (artista == null) return null;
            return new Musica(caminhoArquivo, tamanho, titulo, categoria, duracao, artista);

        } else { // Livro
            String autores = JOptionPane.showInputDialog(this, "Autores (separe por vírgula):");
            if (autores == null) return null;
            return new Livro(caminhoArquivo, tamanho, titulo, categoria, duracao, autores);
        }
    }
}
