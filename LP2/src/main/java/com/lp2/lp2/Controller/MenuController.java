package com.lp2.lp2.Controller;

import com.lp2.lp2.Util.LoaderFXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class MenuController {
    @FXML
    private Button btnCliente;

    @FXML
    private Button btnLance;

    @FXML
    private Button btnLeilao;

    @FXML
    private Button btnSair;

    @FXML
    private void handleBtnLeilao(ActionEvent event) {
        Stage currentStage = getStage();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadCreateLeilao();
        mostrarMensagem("Navegar para Leilão");
    }

    @FXML
    private void handleBtnCliente(ActionEvent event) {
        Stage currentStage = getStage();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadCreateClient();
        mostrarMensagem("Navegar para Cliente");
    }

    @FXML
    private void handleBtnLance(ActionEvent event) {
        Stage currentStage = getStage();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadCreateLance();
        mostrarMensagem("Navegar para Lance");
    }

    @FXML
    private void handleBtnSair(ActionEvent event) {
        // Implementar lógica para sair da aplicação
        mostrarMensagem("Sair da aplicação");
    }

    private void mostrarMensagem(String mensagem) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Informação");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private Stage getStage() {
        return (Stage) btnLance.getScene().getWindow();
    }

    @FXML
    private void handleBtnEditCliente(ActionEvent event) {
        Stage currentStage = getStage();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadEditClient();
        mostrarMensagem("Navegar para Editar Clientes");
    }

    @FXML
    private void handleBtnListCliente(ActionEvent event) {
        Stage currentStage = getStage();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadListClient();
        mostrarMensagem("Navegar para Listar Cliente");
    }

    @FXML
    private void handleBtnListLeilao(ActionEvent event) {
        Stage currentStage = getStage();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadListLeilao();
        mostrarMensagem("Navegar para Listar Leilões");
    }

    @FXML
    private void handleBtnEditLeilao(ActionEvent event) {
        Stage currentStage = getStage();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadEditLeilao();
        mostrarMensagem("Navegar para Editar Leilões");
    }
}