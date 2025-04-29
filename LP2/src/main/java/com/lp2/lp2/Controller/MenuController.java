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
    private Button btnAprovar;

    @FXML
    void handleBtnAprovar(ActionEvent event) {
        Stage currentStage = getStage();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadAprovar();
    }
    @FXML
    private void handleBtnLeilao(ActionEvent event) {
        Stage currentStage = getStage();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadCreateLeilao();

    }

    @FXML
    private void handleBtnCliente(ActionEvent event) {
        Stage currentStage = getStage();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadCreateClient();

    }

    @FXML
    private void handleBtnLance(ActionEvent event) {
        Stage currentStage = getStage();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadCreateLance();

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

    }

    @FXML
    private void handleBtnListCliente(ActionEvent event) {
        Stage currentStage = getStage();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadListClient();

    }

    @FXML
    private void handleBtnListLeilao(ActionEvent event) {
        Stage currentStage = getStage();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadListLeilao();

    }

    @FXML
    private void handleBtnEditLeilao(ActionEvent event) {
        Stage currentStage = getStage();
        LoaderFXML loader = new LoaderFXML(currentStage);
        //loader.loadEditLeilao();
        loader.loadEstatistica();
            }

    @FXML
    private void handleBtnParticipate(ActionEvent event){
        Stage currentStage = getStage();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadParticipateLeilao();
    }

    @FXML
    private void handleBtnSair(ActionEvent event){
        Stage currentStage = getStage();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadLogin();
    }

    @FXML
    private void handleBtnEnunciado(ActionEvent event){
        Stage currentStage = getStage();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadEnunciado();
    }
}