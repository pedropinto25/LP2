package com.lp2.lp2.Controller.Login;

import com.lp2.lp2.Controller.Login.UserEncryption;
import com.lp2.lp2.DAO.ClienteDAO;
import com.lp2.lp2.Model.Cliente;
import com.lp2.lp2.Util.LoaderFXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Date;
import java.sql.SQLException;

public class RegistarClienteController {

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

    public RegistarClienteController() throws SQLException {
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
            mostrarMensagemSucesso("Cliente Registado com sucesso!");
            UserEncryption encryptionService = new UserEncryption();
            encryptionService.encryptPasswords();
            encryptionService.encryptPasswordsCliente();
        } catch (Exception e) {
            mostrarMensagemErro("Erro ao Registar cliente: " + e.getMessage());
            System.out.println(e.getMessage());
        }
    }

    private void mostrarMensagemSucesso(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sucesso");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();

        // Obter o Stage de qualquer nodo da cena, como nomeField
        Stage currentStage = (Stage) nomeField.getScene().getWindow();

        // Redirecionar para a tela de login
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadLogin();
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