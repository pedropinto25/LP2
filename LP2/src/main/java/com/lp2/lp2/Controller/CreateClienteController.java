package com.lp2.lp2.Controller;

import com.lp2.lp2.Model.Cliente;
import com.lp2.lp2.DAO.ClienteDAO;
import com.lp2.lp2.Util.LoaderFXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import java.sql.Date;
import java.sql.SQLException;

public class CreateClienteController {

    @FXML
    private TextField nomeField;
    @FXML
    private TextField moradaField;
    @FXML
    private DatePicker dataNascimentoField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField senhaField;
    @FXML
    private Button btnBack;

    private ClienteDAO clienteDAO;

    public CreateClienteController() throws SQLException {
        clienteDAO = new ClienteDAO();
    }

    @FXML
    private void adicionarCliente() {
        try {
            Cliente cliente = new Cliente();
            cliente.setNome(nomeField.getText());
            cliente.setMorada(moradaField.getText());
            cliente.setDataNascimento(Date.valueOf(dataNascimentoField.getValue()));
            cliente.setEmail(emailField.getText());
            cliente.setSenha(senhaField.getText());
            clienteDAO.addCliente(cliente);
            mostrarMensagemSucesso("Cliente adicionado com sucesso!");
        } catch (Exception e) {
            mostrarMensagemErro("Erro ao adicionar cliente: " + e.getMessage());
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