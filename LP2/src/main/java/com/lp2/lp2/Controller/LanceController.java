package com.lp2.lp2.Controller;

import com.lp2.lp2.DAO.LanceDAO;
import com.lp2.lp2.Model.Lance;
import com.lp2.lp2.Util.LoaderFXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;

public class LanceController {
    @FXML
    private TextField valorField;
    @FXML
    private DatePicker dataHoraField;
    @FXML
    private TextField clienteIdField;
    @FXML
    private TextField leilaoIdField;
    @FXML
    private Button btnBack;

    private LanceDAO lanceDAO;

    public LanceController() throws SQLException {
        lanceDAO = new LanceDAO();
    }

    @FXML
    private void adicionarLance() {
        try {
            Lance lance = new Lance();
            lance.setValor(new BigDecimal(valorField.getText()));
            lance.setDataHora(Timestamp.valueOf(dataHoraField.getValue().atStartOfDay()));
            lance.setClienteId(Integer.parseInt(clienteIdField.getText()));
            lance.setLeilaoId(Integer.parseInt(leilaoIdField.getText()));
            lanceDAO.addLance(lance);
            mostrarMensagemSucesso("Lance adicionado com sucesso!");
        } catch (Exception e) {
            mostrarMensagemErro("Erro ao adicionar lance: " + e.getMessage());
            System.out.println(e.getMessage());
        }
    }

    private void mostrarMensagemSucesso(String mensagem) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Sucesso");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void mostrarMensagemErro(String mensagem) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    public void handleBtnMenu(ActionEvent actionEvent) {
        // Implementar lógica para o botão de menu
    }

    @FXML
    void handleBtnBack(ActionEvent actionEvent) {
        Stage currentStage = (Stage) btnBack.getScene().getWindow();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadMainMenu();
    }
}