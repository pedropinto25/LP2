package com.lp2.lp2.Controller;

import com.lp2.lp2.DAO.LeilaoDAO;
import com.lp2.lp2.Model.Leilao;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;

public class LeilaoController {
    @FXML
    private TextField nomeField;
    @FXML
    private TextField descricaoField;
    @FXML
    private ComboBox<String> tipoField;
    @FXML
    private DatePicker dataInicioField;
    @FXML
    private DatePicker dataFimField;
    @FXML
    private TextField valorMinimoField;
    @FXML
    private TextField valorMaximoField;
    @FXML
    private TextField multiploLanceField;

    private LeilaoDAO leilaoDAO;

    public LeilaoController() throws SQLException {
        leilaoDAO = new LeilaoDAO();
    }

    @FXML
    private void adicionarLeilao() {
        try {
            Leilao leilao = new Leilao();
            leilao.setNome(nomeField.getText());
            leilao.setDescricao(descricaoField.getText());
            leilao.setTipo(tipoField.getValue());
            leilao.setDataInicio(Date.valueOf(dataInicioField.getValue()));
            leilao.setDataFim(Date.valueOf(dataFimField.getValue()));
            leilao.setValorMinimo(new BigDecimal(valorMinimoField.getText()));
            leilao.setValorMaximo(new BigDecimal(valorMaximoField.getText()));
            leilao.setMultiploLance(new BigDecimal(multiploLanceField.getText()));
            leilaoDAO.addLeilao(leilao);
            mostrarMensagemSucesso("Leilão adicionado com sucesso!");
        } catch (Exception e) {
            mostrarMensagemErro("Erro ao adicionar leilão: " + e.getMessage());
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

    public void handleBtnBack(ActionEvent actionEvent) {
        // Implementar lógica para o botão de retroceder
    }
}