package view;

import model.Filme;
import model.Livro;
import model.Midia;
import model.Musica;

import javax.swing.*;
import java.awt.*;
import java.io.File;


 //Formulário modal para criar/editar uma Midia.
 // Agora, em modo edição (mFornecida != null) preenche os campos e atualiza a própria instância.
 
public class FormMidia extends JDialog {//Jdialog por ser janela secundaria
    private Class<?> tipo; // tipo a ser criado (Filme.class, Musica.class, Livro.class)

    public FormMidia(Frame parent, Class<?> tipo) {
        super(parent, true);
        this.tipo = tipo;
        setTitle("Formulário de Mídia");
        setSize(400,300);
        setLocationRelativeTo(parent);
    }

   
     //Mostrar o formulário.
    //  Se mFornecida == null -> modo criação (retorna nova Midia ou null)
    // Se mFornecida != null -> modo edição (altera os atributos da instância e retorna ela)
     
    public Midia mostrar(Midia mFornecida) {
        // Se estamos em edição, não pedimos arquivo (mantemos caminho atual)
        File f = null;
        if (mFornecida == null) {
            JFileChooser chooser = new JFileChooser();
            int res = chooser.showOpenDialog(this);
            if (res != JFileChooser.APPROVE_OPTION) return null;
            f = chooser.getSelectedFile();
        }

        // Valores iniciais (se edição)
        String tituloInit = mFornecida != null ? mFornecida.getTitulo() : "";
        String categoriaInit = mFornecida != null ? mFornecida.getCategoria() : "";
        String duracaoInit = mFornecida != null ? String.valueOf(mFornecida.getDuracao()) : "";

        String titulo = (String) JOptionPane.showInputDialog(this, "Título:", "Título",
                JOptionPane.PLAIN_MESSAGE, null, null, tituloInit);
        if (titulo == null) return null;

        String categoria = (String) JOptionPane.showInputDialog(this, "Categoria:", "Categoria",
                JOptionPane.PLAIN_MESSAGE, null, null, categoriaInit);
        if (categoria == null) return null;

        String duracaoStr = (String) JOptionPane.showInputDialog(this, "Duração (int - minutos/segundos/páginas):",
                "Duração", JOptionPane.PLAIN_MESSAGE, null, null, duracaoInit);
        if (duracaoStr == null) return null;
        int duracao;
        try {
            duracao = Integer.parseInt(duracaoStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Duração inválida.", "Erro", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        // Determina o tipo (se não informado então pergunta)
        Class<?> finalTipo = tipo;
        if (finalTipo == null) {
            String[] op = {"Filme", "Musica", "Livro"};
            int escolha = JOptionPane.showOptionDialog(this, "Tipo de mídia", "Escolha",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, op, op[0]);
            if (escolha == 0) finalTipo = Filme.class;
            else if (escolha == 1) finalTipo = Musica.class;
            else if (escolha == 2) finalTipo = Livro.class;
            else return null;
        }

        double tamanho;
        String caminhoArquivo;

        if (mFornecida != null) {
            // modo edição: mantemos caminho e tamanho existentes
            caminhoArquivo = mFornecida.getCaminhoArquivo();
            tamanho = mFornecida.getTamanhoEmDisco();
        } else {
            tamanho = f.length() / 1024.0;
            caminhoArquivo = f.getAbsolutePath();
        }

        // Agora trata atributos específicos e retornos:
        if (mFornecida != null) {
            // EDIÇÃO: alterar a própria instância (usa setters) e retorna-a
            mFornecida.setTitulo(titulo);
            mFornecida.setCategoria(categoria);
            mFornecida.setDuracao(duracao);
            // atualizar atributos específicos
            if (mFornecida instanceof Filme) {
                String idiomaInit = ((Filme) mFornecida).getIdiomaAudio();
                String idioma = (String) JOptionPane.showInputDialog(this, "Idioma do áudio:", "Idioma",
                        JOptionPane.PLAIN_MESSAGE, null, null, idiomaInit);
                if (idioma == null) return null;
                ((Filme) mFornecida).setIdiomaAudio(idioma);
            } else if (mFornecida instanceof Musica) {
                String artistaInit = ((Musica) mFornecida).getArtista();
                String artista = (String) JOptionPane.showInputDialog(this, "Artista:", "Artista",
                        JOptionPane.PLAIN_MESSAGE, null, null, artistaInit);
                if (artista == null) return null;
                ((Musica) mFornecida).setArtista(artista);
            } else if (mFornecida instanceof Livro) {
                String autoresInit = ((Livro) mFornecida).getAutores();
                String autores = (String) JOptionPane.showInputDialog(this, "Autores (separe por vírgula):", "Autores",
                        JOptionPane.PLAIN_MESSAGE, null, null, autoresInit);
                if (autores == null) return null;
                ((Livro) mFornecida).setAutores(autores);
            }
            // caminho/tamanho não alterados aqui (poderia permitir alterar se quiser)
            return mFornecida;
        } else {
            // CRIAÇÃO: retorna nova instância conforme tipo
            if (finalTipo == Filme.class) {
                String idioma = JOptionPane.showInputDialog(this, "Idioma do áudio:");
                if (idioma == null) return null;
                return new Filme(caminhoArquivo, tamanho, titulo, categoria, duracao, idioma);
            } else if (finalTipo == Musica.class) {
                String artista = JOptionPane.showInputDialog(this, "Artista:");
                if (artista == null) return null;
                return new Musica(caminhoArquivo, tamanho, titulo, categoria, duracao, artista);
            } else {
                String autores = JOptionPane.showInputDialog(this, "Autores (separe por vírgula):");
                if (autores == null) return null;
                return new Livro(caminhoArquivo, tamanho, titulo, categoria, duracao, autores);
            }
        }
    }
}
