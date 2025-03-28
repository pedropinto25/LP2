package com.lp2.lp2.Controller;

import com.lp2.lp2.DAO.ClienteDAO;
import com.lp2.lp2.Model.Cliente;
import com.lp2.lp2.Util.LoaderFXML;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;

import static javafx.collections.FXCollections.observableArrayList;

public class EditClienteController {

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnBack;

    @FXML
    private Button btnMenu;

    @FXML
    private DatePicker dataNascimentoField;

    @FXML
    private TextField emailField;

    @FXML
    private ChoiceBox<Integer> idChoiceBox;

    @FXML
    private TextField moradaField;

    @FXML
    private TextField nomeField;

    @FXML
    private TextField senhaField;

    private ClienteDAO clienteDAO;

    public EditClienteController() throws SQLException {
        clienteDAO = new ClienteDAO();
    }

    @FXML
    public void initialize() {
        populateIdChoiceBox();

        // Adiciona listener para atualizar campos ao selecionar um cliente
        idChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                fillClienteDetails(newValue);
            }
        });
    }

    private void populateIdChoiceBox() {
        try {
            ObservableList<Integer> clienteIds = observableArrayList();
            for (Cliente cliente : clienteDAO.getAllClientes()) {
                clienteIds.add(cliente.getId());
            }
            idChoiceBox.setItems(clienteIds);
        } catch (SQLException e) {
            mostrarMensagemErro("Erro ao carregar IDs dos clientes: " + e.getMessage());
        }
    }

    private void fillClienteDetails(int id) {
        try {
            Cliente cliente = clienteDAO.getClienteById(id);
            if (cliente != null) {
                nomeField.setText(cliente.getNome());
                moradaField.setText(cliente.getMorada());
                dataNascimentoField.setValue(cliente.getDataNascimento().toLocalDate());
                emailField.setText(cliente.getEmail());
                senhaField.setText(cliente.getSenha());
            } else {
                mostrarMensagemErro("Cliente não encontrado!");
            }
        } catch (SQLException e) {
            mostrarMensagemErro("Erro ao carregar detalhes do cliente: " + e.getMessage());
        }
    }

    @FXML
    void editarCliente(ActionEvent event) {
        try {
            int id = idChoiceBox.getValue();
            Cliente cliente = clienteDAO.getClienteById(id);
            if (cliente != null) {
                cliente.setNome(nomeField.getText());
                cliente.setMorada(moradaField.getText());
                cliente.setDataNascimento(Date.valueOf(dataNascimentoField.getValue()));
                cliente.setEmail(emailField.getText());
                cliente.setSenha(senhaField.getText());
                clienteDAO.updateCliente(cliente);
                mostrarMensagemSucesso("Cliente atualizado com sucesso!");
            } else {
                mostrarMensagemErro("Cliente não encontrado!");
            }
        } catch (Exception e) {
            mostrarMensagemErro("Erro ao atualizar cliente: " + e.getMessage());
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