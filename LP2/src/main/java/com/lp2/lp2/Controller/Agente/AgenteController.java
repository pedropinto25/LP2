package com.lp2.lp2.Controller.Agente;

import com.lp2.lp2.DAO.AgenteDAO;
import com.lp2.lp2.Model.Agente;
import com.lp2.lp2.Session.Session;
import com.lp2.lp2.Util.LoaderFXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.sql.SQLException;

public class AgenteController {

    @FXML
    private TextField leilaoIdField;
    @FXML
    private TextField incrementoField;
    @FXML
    private TextField limiteField;
    @FXML
    private Button btnRegistrar;
    @FXML
    private Button btnBack;

    private final AgenteDAO agenteDAO;

    public AgenteController() throws SQLException {
        this.agenteDAO = new AgenteDAO();
    }

    @FXML
    void handleRegistrar(ActionEvent event) {
        try {
            Agente agente = new Agente();
            agente.setClienteId(Session.getLoggedUserId());
            agente.setLeilaoId(Integer.parseInt(leilaoIdField.getText()));
            agente.setIncremento(new BigDecimal(incrementoField.getText()));
            agente.setLimite(new BigDecimal(limiteField.getText()));
            agente.setAtivo(true);
            agenteDAO.addAgente(agente);
            showAlert("Agente registrado com sucesso!");
        } catch (Exception e) {
            showAlert("Erro ao registrar agente: " + e.getMessage());
        }
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    @FXML
    void handleBtnBack(ActionEvent event) {
        Stage stage = (Stage) btnBack.getScene().getWindow();
        LoaderFXML loader = new LoaderFXML(stage);
        loader.loadMenuCliente();
    }
}