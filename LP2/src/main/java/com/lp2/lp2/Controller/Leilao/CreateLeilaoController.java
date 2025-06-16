package com.lp2.lp2.Controller.Leilao;

import com.lp2.lp2.DAO.LeilaoDAO;
import com.lp2.lp2.Model.Leilao;
import com.lp2.lp2.Util.LoaderFXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import com.lp2.lp2.DAO.CategoriaDAO;
import com.lp2.lp2.Model.Categoria;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;

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
    private ListView<Categoria> categoriaList;
    @FXML
    private Button btnBack;

    private LeilaoDAO leilaoDAO;
    private CategoriaDAO categoriaDAO;

    public CreateLeilaoController() throws SQLException {
        leilaoDAO = new LeilaoDAO();
        categoriaDAO = new CategoriaDAO();
    }

    @FXML
    public void initialize() {
        tipoField.getItems().addAll("Online", "Carta Fechada", "Venda Direta");
        try {
                categoriaList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
                    categoriaList.getItems().addAll(categoriaDAO.getAllCategorias());
               } catch (SQLException e) {
                  System.err.println("Erro ao carregar categorias: " + e.getMessage());
               }
        tipoField.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if ("Online".equals(newValue)) {
                multiploLanceField.setDisable(false);
                valorMaximoField.setDisable(true);
                valorMaximoField.clear();
                valorMinimoField.setDisable(false);
            } else if ("Carta Fechada".equals(newValue)) {
                multiploLanceField.setDisable(true);
                multiploLanceField.clear();
                valorMaximoField.setDisable(true);
                valorMaximoField.clear();
                valorMinimoField.setDisable(false);
            } else if ("Venda Direta".equals(newValue)) {
                multiploLanceField.setDisable(true);
                multiploLanceField.clear();
                valorMaximoField.setDisable(true);
                valorMaximoField.clear();
                valorMinimoField.setDisable(false);
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

            leilao.setCategorias(new ArrayList<>(categoriaList.getSelectionModel().getSelectedItems()));

            leilao.setInativo(false); // Definir como ativo por padrão
            leilao.setVendido(false); // define o leilão como não vendido!
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