package com.lp2.lp2.Controller.Cliente;

import com.lp2.lp2.Controller.Login.UserEncryption;
import com.lp2.lp2.DAO.ClienteDAO;
import com.lp2.lp2.Model.Cliente;
import com.lp2.lp2.Session.Session;
import com.lp2.lp2.Util.LoaderFXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.Date;
import java.sql.SQLException;

public class EditDadosController {

    @FXML
    private Button btnSalvar;

    @FXML
    private Button btnBack;

    @FXML
    private Button btnMenu;

    @FXML
    private DatePicker dataNascimentoField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField moradaField;

    @FXML
    private TextField nomeField;

    @FXML
    private TextField senhaField;

    @FXML
    private TextField idField;

    private ClienteDAO clienteDAO;
    private Cliente clienteAtual;

    public EditDadosController() throws SQLException {
        clienteDAO = new ClienteDAO();
    }

    @FXML
    public void initialize() {
        try {
            int clienteId = Session.getUser().getId();
            clienteAtual = clienteDAO.getClienteById(clienteId);
            if (clienteAtual != null) {
                idField.setText(String.valueOf(clienteAtual.getId()));
                nomeField.setText(clienteAtual.getNome());
                moradaField.setText(clienteAtual.getMorada());
                dataNascimentoField.setValue(clienteAtual.getDataNascimento().toLocalDate());
                emailField.setText(clienteAtual.getEmail());
            } else {
                mostrarMensagemErro("Cliente não encontrado!");
            }
        } catch (SQLException e) {
            mostrarMensagemErro("Erro ao carregar dados do cliente: " + e.getMessage());
        }
    }


    @FXML
    void salvarAlteracoes(ActionEvent event) {
        try {

            clienteAtual.setNome(nomeField.getText());
            clienteAtual.setMorada(moradaField.getText());
            clienteAtual.setDataNascimento(Date.valueOf(dataNascimentoField.getValue()));
            clienteAtual.setEmail(emailField.getText());
            clienteAtual.setSenha(senhaField.getText());

            clienteDAO.updateCliente(clienteAtual);
            mostrarMensagemSucesso("Dados atualizados com sucesso!");

            UserEncryption encryptionService = new UserEncryption();
            encryptionService.encryptPasswords();
            encryptionService.encryptPasswordsCliente();

        } catch (Exception e) {
            mostrarMensagemErro("Erro ao atualizar dados: " + e.getMessage());
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
        loader.loadMenuCliente();
    }

    @FXML
    void handleBtnMenu(ActionEvent event) {
        // Implementar se necessário
    }
}
