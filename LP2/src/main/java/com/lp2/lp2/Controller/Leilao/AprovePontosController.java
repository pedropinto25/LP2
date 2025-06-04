package com.lp2.lp2.Controller.Leilao;

import com.lp2.lp2.DAO.PontosDAO;
import com.lp2.lp2.Model.Pontos;
import com.lp2.lp2.Service.EmailNotificationService;
import com.lp2.lp2.Util.LoaderFXML;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.sql.SQLException;

public class AprovePontosController {

    @FXML
    private TableView<Pontos> pontosTableView;
    @FXML
    private TableColumn<Pontos, Integer> idColumn;
    @FXML
    private TableColumn<Pontos, Integer> clienteIdColumn;
    @FXML
    private TableColumn<Pontos, Integer> pontosColumn;
    @FXML
    private TableColumn<Pontos, Boolean> aprovadoColumn;

    @FXML
    private Button btnAprovar;
    @FXML
    private Button btnBack;
    @FXML
    private TextField searchField;

    private PontosDAO pontosDAO;
    private EmailNotificationService emailNotificationService;


    public AprovePontosController() throws SQLException {
        pontosDAO = new PontosDAO();
        emailNotificationService = new EmailNotificationService();

    }

    @FXML
    private void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        clienteIdColumn.setCellValueFactory(new PropertyValueFactory<>("clienteId"));
        pontosColumn.setCellValueFactory(new PropertyValueFactory<>("pontos"));
        aprovadoColumn.setCellValueFactory(new PropertyValueFactory<>("approved"));

        pontosTableView.setItems(loadPontosParaAprovar());
    }

    private ObservableList<Pontos> loadPontosParaAprovar() {
        try {
            // Supondo que há um método em PontosDAO para buscar só não aprovados
            // return FXCollections.observableArrayList(pontosDAO.getAllPontosToAprove());
            // Se não existe ainda:
            return FXCollections.observableArrayList(
                    pontosDAO.getAllPontos().stream()
                            .filter(p -> !p.isApproved()) // implementa o get/set isApproved em Pontos
                            .toList()
            );
        } catch (Exception e) {
            mostrarMensagemErro("Erro ao carregar pontos: " + e.getMessage());
            return FXCollections.observableArrayList();
        }
    }

    @FXML
    public void handleSearch(ActionEvent event) {
        String searchText = searchField.getText().toLowerCase();

        ObservableList<Pontos> filteredList = FXCollections.observableArrayList();

        for (Pontos pontos : loadPontosParaAprovar()) {
            boolean matches = false;

            if (String.valueOf(pontos.getClienteId()).contains(searchText)) matches = true;
            else if (String.valueOf(pontos.getId()).contains(searchText)) matches = true;

            if (matches) filteredList.add(pontos);
        }

        pontosTableView.setItems(filteredList);
    }

    @FXML
    void handleBtnAprovar(ActionEvent event) {
        Pontos selectedPontos = pontosTableView.getSelectionModel().getSelectedItem();

        if (selectedPontos != null) {
            try {
                pontosDAO.aprovarPontos(selectedPontos.getId());
                selectedPontos.setApproved(true);
                pontosTableView.getItems().remove(selectedPontos);

                // Buscar e-mail e nome do Cliente via ClienteDAO
                com.lp2.lp2.DAO.ClienteDAO clienteDAO = new com.lp2.lp2.DAO.ClienteDAO();
                com.lp2.lp2.Model.Cliente cliente = clienteDAO.getClienteById(selectedPontos.getClienteId());

                if (cliente != null && cliente.getEmail() != null) {
                    String nomeCliente = cliente.getNome();
                    String emailCliente = cliente.getEmail();

                    // USAR O SERVIÇO PARA ENVIAR O E-MAIL
                    emailNotificationService.enviarEmailAprovacaoPontos(emailCliente, nomeCliente, selectedPontos.getPontos());
                }

                mostrarMensagemSucesso("Pontos aprovados com sucesso! E-mail enviado ao cliente.");
            } catch (SQLException e) {
                mostrarMensagemErro("Erro ao aprovar pontos: " + e.getMessage());
            }
        } else {
            mostrarMensagemErro("Selecione um registo para aprovar.");
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
}