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
    private ChoiceBox<Leilao> idChoiceBox;
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
                valorMaximoField.setDisable(false);
            } else if ("Venda Direta".equals(newValue)) {
                multiploLanceField.setDisable(true);
                multiploLanceField.clear();
                valorMaximoField.setDisable(true);
                valorMaximoField.clear();
            } else {
                multiploLanceField.setDisable(true);
                multiploLanceField.clear();
                valorMaximoField.setDisable(false);
            }
        });

        populateIdChoiceBox();

        idChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                fillLeilaoDetails(newValue.getId());
            }
        });

    }



    private void populateIdChoiceBox() {
        try {
            ObservableList<Leilao> leiloes = observableArrayList();
            leiloes.addAll(leilaoDAO.getAllLeiloes());
            idChoiceBox.setItems(leiloes);
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
            Leilao leilaoSelecionado = idChoiceBox.getValue();
            if (leilaoSelecionado != null) {
                int id = leilaoSelecionado.getId();
                Leilao leilao = leilaoDAO.getLeilaoById(id);
                if (leilao != null) {
                    leilao.setNome(nomeField.getText());
                    leilao.setDescricao(descricaoField.getText());
                    leilao.setTipo(tipoField.getValue());
                    leilao.setDataInicio(Date.valueOf(dataInicioField.getValue()));

                    // Verificar se a data de fim foi definida
                    if (dataFimField.getValue() != null) {
                        leilao.setDataFim(Date.valueOf(dataFimField.getValue()));
                    } else {
                        leilao.setDataFim(null); // Data de fim indefinida
                    }

                    // Verificar se o valor mínimo foi definido
                    BigDecimal valorMinimo = null;
                    if (!valorMinimoField.getText().isEmpty()) {
                        valorMinimo = new BigDecimal(valorMinimoField.getText());
                        leilao.setValorMinimo(valorMinimo);
                    } else if (!"Venda Direta".equals(tipoField.getValue())) {
                        mostrarMensagemErro("O valor mínimo é obrigatório para este tipo de leilão.");
                        return;
                    }

                    // Verificar se o valor máximo foi definido
                    BigDecimal valorMaximo = null;
                    if (!valorMaximoField.getText().isEmpty()) {
                        valorMaximo = new BigDecimal(valorMaximoField.getText());
                        // Verificar se o valor máximo é maior que o valor mínimo, se ambos forem definidos
                        if (valorMinimo != null && valorMaximo.compareTo(valorMinimo) <= 0) {
                            mostrarMensagemErro("O valor máximo deve ser maior que o valor mínimo.");
                            return;
                        }
                        leilao.setValorMaximo(valorMaximo);
                    } else {
                        leilao.setValorMaximo(null); // Valor máximo indefinido
                    }

                    // Verificar o múltiplo de lance se o tipo for "Online"
                    if ("Online".equals(tipoField.getValue())) {
                        BigDecimal multiploLance = new BigDecimal(multiploLanceField.getText());
                        leilao.setMultiploLance(multiploLance);

                        // Verificar se o valor mínimo x múltiplo de lance excede o valor máximo
                        if (valorMaximo != null && valorMinimo != null && valorMinimo.multiply(multiploLance).compareTo(valorMaximo) > 0) {
                            mostrarMensagemErro("O valor mínimo x o múltiplo de lance excede o valor máximo.");
                            return;
                        }
                    } else {
                        leilao.setMultiploLance(null); // Definir como null para outros tipos de leilão
                    }

                    leilaoDAO.updateLeilao(leilao);
                    mostrarMensagemSucesso("Leilão atualizado com sucesso!");
                } else {
                    mostrarMensagemErro("Leilão não encontrado!");
                }
            } else {
                mostrarMensagemErro("Nenhum leilão selecionado!");
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