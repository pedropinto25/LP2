package com.lp2.lp2.Controller.Leilao;

import com.lp2.lp2.DAO.LeilaoDAO;
import com.lp2.lp2.Model.Leilao;
import com.lp2.lp2.Util.LoaderFXML;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.sql.SQLException;

public class ReadDeleteLeilaoController {

    @FXML
    private TableView<Leilao> leilaoTableView;
    @FXML
    private TableColumn<Leilao, Integer> idColumn;
    @FXML
    private TableColumn<Leilao, String> nomeColumn;
    @FXML
    private TableColumn<Leilao, String> descricaoColumn;
    @FXML
    private TableColumn<Leilao, String> tipoColumn;
    @FXML
    private TableColumn<Leilao, String> dataInicioColumn;
    @FXML
    private TableColumn<Leilao, String> dataFimColumn;
    @FXML
    private TableColumn<Leilao, String> valorMinimoColumn;
    @FXML
    private TableColumn<Leilao, String> valorMaximoColumn;
    @FXML
    private TableColumn<Leilao, String> multiploLanceColumn;
    @FXML
    private TableColumn<Leilao, Boolean> inativoColumn; // Nova coluna

    @FXML
    private Button btnMenu;
    @FXML
    private Button btnBack;
    @FXML
    private TextField searchField;

    private LeilaoDAO leilaoDAO;

    public ReadDeleteLeilaoController() throws SQLException {
        leilaoDAO = new LeilaoDAO();
    }

    @FXML
    private void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomeColumn.setCellValueFactory(new PropertyValueFactory<>("nome"));
        descricaoColumn.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        tipoColumn.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        dataInicioColumn.setCellValueFactory(new PropertyValueFactory<>("dataInicio"));
        dataFimColumn.setCellValueFactory(new PropertyValueFactory<>("dataFim"));
        valorMinimoColumn.setCellValueFactory(new PropertyValueFactory<>("valorMinimo"));
        valorMaximoColumn.setCellValueFactory(new PropertyValueFactory<>("valorMaximo"));
        multiploLanceColumn.setCellValueFactory(new PropertyValueFactory<>("multiploLance"));
        inativoColumn.setCellValueFactory(new PropertyValueFactory<>("inativo")); // Configurar nova coluna

        leilaoTableView.setItems(loadLeiloes());
    }

    private ObservableList<Leilao> loadLeiloes() {
        try {
            return FXCollections.observableArrayList(leilaoDAO.getAllLeiloes());
        } catch (SQLException e) {
            mostrarMensagemErro("Erro ao carregar leilões: " + e.getMessage());
            return FXCollections.observableArrayList();
        }
    }

    @FXML
    public void handleSearch(ActionEvent event) {
        String searchText = searchField.getText().toLowerCase();

        ObservableList<Leilao> filteredList = FXCollections.observableArrayList();

        for (Leilao leilao : loadLeiloes()) {
            boolean matches = false;

            if (leilao.getNome().toLowerCase().contains(searchText)) {
                matches = true;
            } else if (String.valueOf(leilao.getId()).contains(searchText)) {
                matches = true;
            } else if (leilao.getDescricao().toLowerCase().contains(searchText)) {
                matches = true;
            }

            if (matches) {
                filteredList.add(leilao);
            }
        }

        leilaoTableView.setItems(filteredList);
    }

    @FXML
    void handleBtnDelete(ActionEvent event) {
        Leilao selectedLeilao = leilaoTableView.getSelectionModel().getSelectedItem();

        if (selectedLeilao != null) {
            try {
                leilaoDAO.deleteLeilao(selectedLeilao.getId());
                leilaoTableView.getItems().remove(selectedLeilao);
                mostrarMensagemSucesso("Leilão eliminado com sucesso!");
            } catch (SQLException e) {
                mostrarMensagemErro("Erro ao eliminar leilão: " + e.getMessage());
            }
        } else {
            mostrarMensagemErro("Por favor, selecione um leilão antes de tentar eliminá-lo.");
        }
    }

    @FXML
    void handleBtnDesativar(ActionEvent event) { // Novo método para desativar
        Leilao selectedLeilao = leilaoTableView.getSelectionModel().getSelectedItem();

        if (selectedLeilao != null) {
            try {
                leilaoDAO.desativarLeilao(selectedLeilao.getId());
                selectedLeilao.setInativo(true); // Atualizar o estado localmente
                leilaoTableView.refresh(); // Atualizar a tabela para refletir a mudança
                mostrarMensagemSucesso("Leilão desativado com sucesso!");
            } catch (SQLException e) {
                mostrarMensagemErro("Erro ao desativar leilão: " + e.getMessage());
            }
        } else {
            mostrarMensagemErro("Por favor, selecione um leilão antes de tentar desativá-lo.");
        }
    }

    private void mostrarMensagemSucesso(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sucesso");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void mostrarMensagemErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
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
