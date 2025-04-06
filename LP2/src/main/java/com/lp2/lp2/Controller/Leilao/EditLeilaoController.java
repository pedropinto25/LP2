package com.lp2.lp2.Controller.Leilao;

import com.lp2.lp2.DAO.LeilaoDAO;
import com.lp2.lp2.Model.Leilao;
import com.lp2.lp2.Util.LoaderFXML;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;

import static javafx.collections.FXCollections.observableArrayList;

public class EditLeilaoController {

    @FXML
    private ChoiceBox<Integer> idChoiceBox;
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
    private Button btnAdd;
    @FXML
    private Button btnMenu;
    @FXML
    private Button btnBack;

    private LeilaoDAO leilaoDAO;

    public EditLeilaoController() throws SQLException {
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

        populateIdChoiceBox();

        idChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                fillLeilaoDetails(newValue);
            }
        });
    }

    private void populateIdChoiceBox() {
        try {
            ObservableList<Integer> leilaoIds = observableArrayList();
            for (Leilao leilao : leilaoDAO.getAllLeiloes()) {
                leilaoIds.add(leilao.getId());
            }
            idChoiceBox.setItems(leilaoIds);
        } catch (SQLException e) {
            mostrarMensagemErro("Erro ao carregar IDs dos leilões: " + e.getMessage());
        }
    }

    private void fillLeilaoDetails(int id) {
        try {
            Leilao leilao = leilaoDAO.getLeilaoById(id);
            if (leilao != null) {
                nomeField.setText(leilao.getNome());
                descricaoField.setText(leilao.getDescricao());
                tipoField.setValue(leilao.getTipo());
                dataInicioField.setValue(leilao.getDataInicio().toLocalDate());
                dataFimField.setValue(leilao.getDataFim() != null ? leilao.getDataFim().toLocalDate() : null);
                valorMinimoField.setText(leilao.getValorMinimo().toString());
                valorMaximoField.setText(leilao.getValorMaximo() != null ? leilao.getValorMaximo().toString() : "");
                if ("Online".equals(leilao.getTipo())) {
                    multiploLanceField.setDisable(false);
                    multiploLanceField.setText(leilao.getMultiploLance() != null ? leilao.getMultiploLance().toString() : "");
                } else {
                    multiploLanceField.setDisable(true);
                    multiploLanceField.clear();
                }
            } else {
                mostrarMensagemErro("Leilão não encontrado!");
            }
        } catch (SQLException e) {
            mostrarMensagemErro("Erro ao carregar detalhes do leilão: " + e.getMessage());
        }
    }

    @FXML
    void editarLeilao(ActionEvent event) {
        try {
            int id = idChoiceBox.getValue();
            Leilao leilao = leilaoDAO.getLeilaoById(id);
            if (leilao != null) {
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
                    leilao.setMultiploLance(null);
                }
                leilaoDAO.updateLeilao(leilao);
                mostrarMensagemSucesso("Leilão atualizado com sucesso!");
            } else {
                mostrarMensagemErro("Leilão não encontrado!");
            }
        } catch (Exception e) {
            mostrarMensagemErro("Erro ao atualizar leilão: " + e.getMessage());
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

    @FXML
    void handleBtnBack(ActionEvent actionEvent) {
        Stage currentStage = (Stage) btnBack.getScene().getWindow();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadMainMenu();
    }

    @FXML
    void handleBtnMenu(ActionEvent event) {
        // Implementar lógica para o botão de menu
    }
}