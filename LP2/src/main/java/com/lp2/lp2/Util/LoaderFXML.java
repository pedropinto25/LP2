package com.lp2.lp2.Util;


import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * A classe LoaderFXML é responsável por carregar e exibir diferentes telas (views) em uma aplicação JavaFX.
 * Utiliza o Stage fornecido para exibir as telas de forma dinâmica. Cada método carrega uma tela específica
 * com um título definido.
 * <p>
 * O método {@link #loadView(String, String)} é usado internamente para carregar qualquer tela,
 * independentemente do tipo de layout (como AnchorPane, VBox, HBox, etc.).
 */
public class LoaderFXML {

    /**
     * Referência ao Stage principal onde as telas serão carregadas
     */
    private Stage primaryStage;

    /**
     * Construtor que inicializa o LoaderFXML com um Stage específico.
     * Este Stage será utilizado para exibir as telas carregadas.
     *
     * @param primaryStage o Stage principal onde as views serão exibidas
     */
    public LoaderFXML(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    /**-------------------------------------------------------------------------------MENUS----------------------------------------------------------------------------------*/

    /**
     * Carrega e exibe o menu principal da aplicação.
     * O arquivo FXML para o menu principal é carregado, e o título da janela é definido como "Menu Principal".
     * Esse método é utilizado para exibir a tela inicial de navegação.
     */
    public void loadMainMenu() {
        loadView("/com/lp3_grupo5/lp2/Menus/Menu_Inicial.fxml", "Menu Principal");
    }

    public void loadCreateClient() {
        loadView("/com/lp3_grupo5/lp2/ClienteViews/AddCliente.fxml", "Criar Cliente");
    }

    public void loadCreateLeilao() {loadView("/com/lp3_grupo5/lp2/LeilaoViews/CreateLeilao.fxml", "Criar Leilão");}

    public void loadCreateLance() {loadView("/com/lp3_grupo5/lp2/LeilaoViews/CreateLance.fxml", "Licitar");}

    public void loadEditClient() {loadView("/com/lp3_grupo5/lp2/ClienteViews/EditCliente.fxml", "Editar Cliente"); }

    public void loadListClient() {loadView("/com/lp3_grupo5/lp2/ClienteViews/ReadCliente.fxml", "Listar Cliente"); }

    public void loadListLeilao() {loadView("/com/lp3_grupo5/lp2/LeilaoViews/ReadLeilao.fxml", "Listar Leilões"); }

    public void loadEditLeilao() {loadView("/com/lp3_grupo5/lp2/LeilaoViews/EditLeilao.fxml", "Editar Leilões"); }

    public void loadParticipateLeilao() {loadView("/com/lp3_grupo5/lp2/LeilaoViews/ParticipateLeilao.fxml", "Participar em Leilões");}

    public void loadLogin() {loadView("/com/lp3_grupo5/lp2/Login/Login.fxml", "Voltar ao login");}

    public void loadRegistar() {loadView("/com/lp3_grupo5/lp2/Login/Registar.fxml", "Registar");}

    public void loadAprovar() {loadView("/com/lp3_grupo5/lp2/ClienteViews/AproveCliente.fxml" , "Aprovar Clientes");}
/** ************************************************************************************************************************************************************************ */
    /**
     * Carrega e exibe uma tela específica (view) definida pelo caminho do arquivo FXML.
     * Este método é genérico e permite a definição de qualquer tela e título,
     * utilizando o caminho FXML e o título fornecidos.
     * <p>
     * O layout carregado pode ser de qualquer tipo de container de layout do JavaFX (como {@link javafx.scene.layout.AnchorPane},
     * {@link javafx.scene.layout.VBox}, etc.). O método utiliza {@link Parent} para garantir a flexibilidade no tipo de layout.
     *
     * @param fxmlPath Caminho do arquivo FXML da tela a ser carregada
     * @param title    Título a ser exibido na janela para a tela carregada
     */
    public void loadView(String fxmlPath, String title) {
        try {
            // Depuração do caminho FXML
            System.out.println("Tentando carregar o FXML: " + fxmlPath);

            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));

            // Verifica se o arquivo FXML foi encontrado
            if (loader.getLocation() == null) {
                System.err.println("Erro: o arquivo FXML não foi encontrado no caminho: " + fxmlPath);
                return;  // Não continua se não encontrar o arquivo
            } else {
                System.out.println("Arquivo FXML encontrado: " + fxmlPath);
            }

            Parent view = loader.load();  // Usando Parent para permitir layouts variados
            Scene scene = new Scene(view);

            // Define a cena e o título do Stage
            primaryStage.setScene(scene);
            primaryStage.setTitle(title);

            // Maximizar a janela
            primaryStage.setMaximized(true);

            primaryStage.show();

        } catch (IOException e) {
            System.err.println("Erro ao carregar o arquivo FXML: " + fxmlPath);
            e.printStackTrace();
        }
    }
}