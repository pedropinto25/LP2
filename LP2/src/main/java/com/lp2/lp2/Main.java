package com.lp2.lp2;

import com.lp2.lp2.Controller.Login.UserEncryption;
import com.lp2.lp2.Util.LoaderFXML;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class Main extends Application {
    private static Stage mainStage;
    @Override
    public void start(Stage primaryStage) throws IOException {
        mainStage = primaryStage;
        loadScene("/com/lp3_grupo5/lp2/Login/Login.fxml");

    }
    /**
     * Método para carregar uma nova cena no `Stage` principal.
     *
     * @param fxmlPath Caminho do arquivo FXML para carregar.
     */
    public static void loadScene(String fxmlPath) {
        try {
            // Carrega o arquivo FXML
            URL resource = Main.class.getResource(fxmlPath);
            if (resource == null) {
                System.out.println("Arquivo FXML não encontrado: " + fxmlPath);
                return;
            }

            FXMLLoader loader = new FXMLLoader(resource);
            Parent root = loader.load();

            // Define nova cena
            Scene scene = new Scene(root);
            mainStage.setScene(scene);

            // Exibe o Stage
            mainStage.setTitle("Leilões Express");

            // Centraliza o Stage na tela (tanto em X quanto em Y)
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            mainStage.setX((screenBounds.getWidth() - mainStage.getWidth()) / 2); // Centraliza no eixo X
            mainStage.setY((screenBounds.getHeight() - mainStage.getHeight()) / 2); // Centraliza no eixo Y

            // Exibe a janela
            mainStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao carregar o FXML: " + e.getMessage());
        }
    }
    public static void main(String[] args) {
        UserEncryption encryptionService = new UserEncryption();
        encryptionService.encryptPasswords();

        launch(args);
    }
}