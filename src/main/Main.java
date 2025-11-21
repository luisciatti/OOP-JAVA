package main;

import model.GerenciadorMidias;
import controller.MidiaController;
import view.TelaPrincipal;


 // Classe com método main() — única classe main no projeto conforme requisito.
 
public class Main {
    public static void main(String[] args) {

        String diretorio = "dados_midias";
        GerenciadorMidias ger = new GerenciadorMidias(diretorio);

        // primeiro cria um controller SEM view
        MidiaController controlador = new MidiaController(ger, null);

        // agora cria a view passando este controller
        TelaPrincipal tela = new TelaPrincipal(controlador);

        // associa a view no controller
        controlador.setViewPrincipal(tela);

        // inicializa tudo
        controlador.inicializar();
    }
}
