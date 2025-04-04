package com.lp2.lp2.Controller.Cliente;

import com.lp2.lp2.DAO.ClienteDAO;
import com.lp2.lp2.Model.Cliente;
import com.lp2.lp2.Session.Session;
import com.lp2.lp2.Model.User;
import com.lp2.lp2.Util.LoaderFXML;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.resource.Emailv31;
import io.github.cdimascio.dotenv.Dotenv;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.SQLException;

public class AproveClienteController {

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
    private TableColumn<Cliente,Boolean> aproveColumn;

    @FXML
    private Button btnAprovar;

    @FXML
    private Button btnMenu;
    @FXML
    private Button btnBack;
    @FXML
    private TextField searchField;

    private ClienteDAO clienteDAO;

    public AproveClienteController() throws SQLException {
        clienteDAO = new ClienteDAO();

    }
    private static final Dotenv dotenv = Dotenv.load();
    @FXML
    private void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomeColumn.setCellValueFactory(new PropertyValueFactory<>("nome"));
        moradaColumn.setCellValueFactory(new PropertyValueFactory<>("morada"));
        dataNascimentoColumn.setCellValueFactory(new PropertyValueFactory<>("dataNascimento"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        senhaColumn.setCellValueFactory(new PropertyValueFactory<>("senha"));
        aproveColumn.setCellValueFactory(new PropertyValueFactory<>("approved"));
        clienteTableView.setItems(loadClientes());

    }





    private ObservableList<Cliente> loadClientes() {
        try {
            return FXCollections.observableArrayList(clienteDAO.getAllClientesToAprove());
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
                clienteDAO.AproveCliente(selectedCliente.getId());
                clienteTableView.getItems().remove(selectedCliente);
                mostrarMensagemSucesso("Cliente Aprovado com sucesso!");
                sendDeactivationEmail(selectedCliente.getEmail(), selectedCliente.getNome());
            } catch (SQLException e) {
                mostrarMensagemErro("Erro ao aprovar cliente: " + e.getMessage());
            }
        } else {
            mostrarMensagemErro("Por favor, selecione um cliente antes de tentar aprovar-lo.");
        }
    }

    private void sendDeactivationEmail(String recipientEmail, String recipientName) {
        String apiKey = dotenv.get("MAILJET_API_KEY");
        String apiSecret = dotenv.get("MAILJET_API_SECRET");

        // Obter o número de identificação do gestor da sessão
        User loggedUser = Session.getUser();
        String managerIdNumber = loggedUser != null ? String.valueOf(loggedUser.getId()) : "ID não disponível";

        MailjetClient client = new MailjetClient(apiKey, apiSecret);
        MailjetRequest request = new MailjetRequest(Emailv31.resource)
                .property(Emailv31.MESSAGES, new JSONArray()
                        .put(new JSONObject()
                                .put(Emailv31.Message.FROM, new JSONObject()
                                        .put("Email", "lp3sendmail@gmail.com")
                                        .put("Name", "Leilões Express"))
                                .put(Emailv31.Message.TO, new JSONArray()
                                        .put(new JSONObject()
                                                .put("Email", recipientEmail)
                                                .put("Name", recipientName)))
                                .put(Emailv31.Message.SUBJECT, "Conta Ativada")
                                .put(Emailv31.Message.TEXTPART, "Olá " + recipientName + ",\n\nA sua conta foi Ativada pelo gestor: " + managerIdNumber + ".\n\nPara mais informações, contacte lp2sendmail@gmail.com")
                                .put(Emailv31.Message.HTMLPART,
                                        "<div style='font-family: Arial, sans-serif; color: #333;'>"
                                                + "<h3>Olá " + recipientName + ",</h3>"
                                                + "<p>A sua conta foi ativada pelo gestor: <strong>" + managerIdNumber + "</strong>.</p>"
                                                + "<p>Para mais informações, contacte <a href='mailto:lp2sendmail@gmail.com'>lp2sendmail@gmail.com</a>.</p>"
                                                + "<br>"
                                                + "<p>Atenciosamente,</p>"
                                                + "<p><strong>Leilões Express</strong></p>"
                                                + "</div>")));

        try {
            MailjetResponse response = client.post(request);
            System.out.println(response.getStatus());
            System.out.println(response.getData());
        } catch (Exception e) {
            e.printStackTrace();
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