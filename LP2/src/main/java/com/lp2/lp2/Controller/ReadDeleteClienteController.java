package com.lp2.lp2.Controller;

import com.lp2.lp2.DAO.ClienteDAO;
import com.lp2.lp2.Model.Cliente;
import com.lp2.lp2.Util.LoaderFXML;
import javafx.beans.property.SimpleStringProperty;
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

public class ReadDeleteClienteController {

    @FXML
    private TableView<Cliente> clienteTableView;
    @FXML
    private TableColumn<Cliente, Integer> idColumn;
    @FXML
    private TableColumn<Cliente, String> nomeColumn;
    @FXML
    private TableColumn<Cliente, String> moradaColumn;
    @FXML
    private TableColumn<Cliente, String> dataNascimentoColumn;
    @FXML
    private TableColumn<Cliente, String> emailColumn;
    @FXML
    private TableColumn<Cliente, String> senhaColumn;

    @FXML
    private Button btnMenu;
    @FXML
    private Button btnBack;
    @FXML
    private TextField searchField;

    private ClienteDAO clienteDAO;

    public ReadDeleteClienteController() throws SQLException {
        clienteDAO = new ClienteDAO();
    }

    @FXML
    private void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomeColumn.setCellValueFactory(new PropertyValueFactory<>("nome"));
        moradaColumn.setCellValueFactory(new PropertyValueFactory<>("morada"));
        dataNascimentoColumn.setCellValueFactory(new PropertyValueFactory<>("dataNascimento"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        senhaColumn.setCellValueFactory(new PropertyValueFactory<>("senha"));

        clienteTableView.setItems(loadClientes());
    }

    private ObservableList<Cliente> loadClientes() {
        try {
            return FXCollections.observableArrayList(clienteDAO.getAllClientes());
        } catch (SQLException e) {
            mostrarMensagemErro("Erro ao carregar clientes: " + e.getMessage());
            return FXCollections.observableArrayList();
        }
    }

    @FXML
    public void handleSearch(ActionEvent event) {
        String searchText = searchField.getText().toLowerCase();

        ObservableList<Cliente> filteredList = FXCollections.observableArrayList();

        for (Cliente cliente : loadClientes()) {
            boolean matches = false;

            if (cliente.getNome().toLowerCase().contains(searchText)) {
                matches = true;
            } else if (String.valueOf(cliente.getId()).contains(searchText)) {
                matches = true;
            } else if (cliente.getEmail().toLowerCase().contains(searchText)) {
                matches = true;
            }

            if (matches) {
                filteredList.add(cliente);
            }
        }

        clienteTableView.setItems(filteredList);
    }

    @FXML
    void handleBtnDelete(ActionEvent event) {
        Cliente selectedCliente = clienteTableView.getSelectionModel().getSelectedItem();

        if (selectedCliente != null) {
            try {
                clienteDAO.deleteCliente(selectedCliente.getId());
                clienteTableView.getItems().remove(selectedCliente);
                mostrarMensagemSucesso("Cliente eliminado com sucesso!");
            } catch (SQLException e) {
                mostrarMensagemErro("Erro ao eliminar cliente: " + e.getMessage());
            }
        } else {
            mostrarMensagemErro("Por favor, selecione um cliente antes de tentar eliminá-lo.");
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