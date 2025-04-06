package com.lp2.lp2.Controller.Leilao;

import com.lp2.lp2.DAO.LeilaoDAO;
import com.lp2.lp2.Model.Leilao;
import com.lp2.lp2.Util.LoaderFXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;

public class CreateLeilaoController {
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
    @FXML
    private Button btnBack;

    private LeilaoDAO leilaoDAO;

    public CreateLeilaoController() throws SQLException {
        leilaoDAO = new LeilaoDAO();
    }

    @FXML
    public void initialize() {
        tipoField.getItems().addAll("Online", "Carta Fechada", "Venda Direta");
        tipoField.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if ("Online".equals(newValue)) {
                multiploLanceField.setDisable(false);
            } else {
                multiploLanceField.setDisable(true);
                multiploLanceField.clear();
            }
        });
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
            if ("Online".equals(tipoField.getValue())) {
                leilao.setMultiploLance(new BigDecimal(multiploLanceField.getText()));
            } else {
                leilao.setMultiploLance(null); // Definir como null para outros tipos de leilão
            }
            leilao.setInativo(false); // Definir como ativo por padrão
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

    @FXML
    void handleBtnBack(ActionEvent actionEvent) {
        Stage currentStage = (Stage) btnBack.getScene().getWindow();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadMainMenu();
    }
}