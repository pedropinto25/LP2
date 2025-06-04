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
    private Button btnAprovar;

    @FXML
    private Button btnCliente;

    @FXML
    private Button btnEditCliente;

    @FXML
    private Button btnEditLeilao;

    @FXML
    private Button btnEnunciado;

    @FXML
    private Button btnEstatisticas;

    @FXML
    private Button btnLance;

    @FXML
    private Button btnLeilao;

    @FXML
    private Button btnListCliente;

    @FXML
    private Button btnListLeilao;

    @FXML
    private Button btnParticipate;

    @FXML
    private Button btnSair;

    @FXML
    void handleBtnAprovar(ActionEvent actionEvent) {
        Stage currentStage = (Stage) btnAprovar.getScene().getWindow();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadAprovar();
    }
    @FXML
    private void handleBtnLeilao(ActionEvent actionEvent) {
        Stage currentStage = (Stage) btnLeilao.getScene().getWindow();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadCreateLeilao();

    }

    @FXML
    private void handleBtnCliente(ActionEvent actionEvent) {
        Stage currentStage = (Stage) btnCliente.getScene().getWindow();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadCreateClient();

    }

    @FXML
    private void handleBtnLance(ActionEvent actionEvent) {
        Stage currentStage = (Stage) btnLance.getScene().getWindow();
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

    @FXML
    private void handleBtnEditCliente(ActionEvent actionEvent) {
        Stage currentStage = (Stage) btnEditCliente.getScene().getWindow();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadEditClient();

    }

    @FXML
    private void handleBtnListCliente(ActionEvent actionEvent) {
        Stage currentStage = (Stage) btnListCliente.getScene().getWindow();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadListClient();

    }

    @FXML
    private void handleBtnListLeilao(ActionEvent actionEvent) {
        Stage currentStage = (Stage) btnListLeilao.getScene().getWindow();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadListLeilao();

    }

    @FXML
    private void handleBtnEditLeilao(ActionEvent actionEvent) {
        Stage currentStage = (Stage) btnEditLeilao.getScene().getWindow();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadEditLeilao();
            }

    @FXML
    private void handleBtnParticipate(ActionEvent actionEvent){
        Stage currentStage = (Stage) btnParticipate.getScene().getWindow();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadParticipateLeilao();
    }

    @FXML
    private void handleBtnSair(ActionEvent actionEvent){
        Stage currentStage = (Stage) btnSair.getScene().getWindow();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadLogin();
    }

    @FXML
    private void handleBtnEnunciado(ActionEvent actionEvent){
        Stage currentStage = (Stage) btnEnunciado.getScene().getWindow();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadEnunciado();
    }
    @FXML
    private void handleBtnEstatisticas(ActionEvent actionEvent) {
        Stage currentStage = (Stage) btnEstatisticas.getScene().getWindow();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadEstatistica();
    }

}