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
    private void handleBtnListLeilao(ActionEvent actionEvent) {
        Stage currentStage = (Stage) btnListLeilao.getScene().getWindow();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadListLeilao();

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
        loader.loadParticipateLeilao();
    }

    @FXML
    void handleBtnSair(ActionEvent actionEvent) {
        Stage currentStage = (Stage) btnSair.getScene().getWindow();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadLogin();
    }

}
