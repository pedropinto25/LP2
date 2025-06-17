package com.lp2.lp2.Controller;

import com.lp2.lp2.Util.LoaderFXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class MenuClienteController {

    @FXML
    private Button btnListLeilao;

    @FXML
    private Button btnParticipate;

    @FXML
    private Button btnAgente;

    @FXML
    private Button btnSair;

    @FXML
    private Button btnInscritos;

    @FXML
    private Button btnEditarDados;

    @FXML
    private void handleBtnListLeilao(ActionEvent actionEvent) {
        Stage currentStage = (Stage) btnListLeilao.getScene().getWindow();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadListLeilaoCliente();

    }

    @FXML
    private void handleBtnAgente(ActionEvent actionEvent){
        Stage currentStage = (Stage) btnAgente.getScene().getWindow();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadConfigAgente();
    }
    @FXML
    private void handleBtnParticipate(ActionEvent actionEvent){
        Stage currentStage = (Stage) btnParticipate.getScene().getWindow();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadParticipateLeilaoCliente();
    }

    @FXML
    void handleBtnSair(ActionEvent actionEvent) {
        Stage currentStage = (Stage) btnSair.getScene().getWindow();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadLogin();
    }
    @FXML
    private void handleBtnNegociar(ActionEvent actionEvent) {
        Stage currentStage = (Stage) btnParticipate.getScene().getWindow();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadnegoCliente();
    }

    @FXML
    private void handleBtnInscritos(ActionEvent actionEvent) {
        Stage currentStage = (Stage) btnInscritos.getScene().getWindow();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadLeilaoInscrito();
    }

    @FXML
    void handleBtnEditarDados(ActionEvent event) {
        Stage currentStage = (Stage) btnEditarDados.getScene().getWindow();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadEditDados();

    }
}
