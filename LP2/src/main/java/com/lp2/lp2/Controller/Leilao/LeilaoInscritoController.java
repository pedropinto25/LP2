package com.lp2.lp2.Controller.Leilao;

import com.lp2.lp2.DAO.LeilaoDAO;
import com.lp2.lp2.DAO.LeilaoParticipacaoDAO;
import com.lp2.lp2.Model.Leilao;
import com.lp2.lp2.Model.LeilaoParticipacao;
import com.lp2.lp2.Session.Session;
import com.lp2.lp2.Util.LoaderFXML;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LeilaoInscritoController {

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
    private TableColumn<Leilao, String> inativoColumn;
    @FXML
    private TableColumn<Leilao, String> vendidoColumn;

    @FXML
    private TextField searchField;
    @FXML
    private Button btnBack;
    @FXML
    private Button btnMenu;

    @FXML
    private Button btnAvaliar;


    private LeilaoParticipacaoDAO participacaoDAO;
    private LeilaoDAO leilaoDAO;

    public LeilaoInscritoController() throws SQLException {
        participacaoDAO = new LeilaoParticipacaoDAO();
        leilaoDAO = new LeilaoDAO();
    }

    @FXML
    private void initialize() {
        leilaoTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> onLeilaoSelecionado());
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNome()));
        descricaoColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescricao()));
        tipoColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTipo()));
        dataInicioColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getDataInicio())));
        dataFimColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getDataFim())));
        valorMinimoColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getValorMinimo())));
        valorMaximoColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getValorMaximo())));
        multiploLanceColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getMultiploLance())));
        inativoColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getInativo() ? "Sim" : "Não"));
        vendidoColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getVendido() ? "Sim" : "Não"));

        leilaoTableView.setItems(loadLeiloesInscritos());
    }

    private ObservableList<Leilao> loadLeiloesInscritos() {
        List<Leilao> leiloesInscritos = new ArrayList<>();
        try {
            int clienteId = Session.getUser().getId();
            List<LeilaoParticipacao> participacoes = participacaoDAO.getAllParticipacoes();
            for (LeilaoParticipacao p : participacoes) {
                if (p.getClienteId() == clienteId) {
                    Leilao leilao = leilaoDAO.getLeilaoById(p.getLeilaoId());
                    if (leilao != null) {
                        leiloesInscritos.add(leilao);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return FXCollections.observableArrayList(leiloesInscritos);
    }

    @FXML
    private void handleSearch() {
        String searchText = searchField.getText().toLowerCase();
        ObservableList<Leilao> filteredList = FXCollections.observableArrayList();

        for (Leilao leilao : loadLeiloesInscritos()) {
            boolean matches = leilao.getNome().toLowerCase().contains(searchText)
                    || String.valueOf(leilao.getId()).contains(searchText)
                    || leilao.getDescricao().toLowerCase().contains(searchText);

            if (matches) {
                filteredList.add(leilao);
            }
        }

        leilaoTableView.setItems(filteredList);
    }

    @FXML
    void handleBtnBack(ActionEvent actionEvent) {
        Stage currentStage = (Stage) btnBack.getScene().getWindow();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadMainMenu();
    }

    @FXML
    private void handleBtnAvaliar() {
        Leilao selectedLeilao = leilaoTableView.getSelectionModel().getSelectedItem();
        if (selectedLeilao != null && selectedLeilao.getVendido()) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/lp3_grupo5/lp2/LeilaoViews/RatingDialog.fxml"));
                Parent root = loader.load();

                RatingController controller = loader.getController();
                controller.setLeilaoId(selectedLeilao.getId());

                Stage stage = new Stage();
                stage.setTitle("Avaliar Leilão");
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @FXML
    private void onLeilaoSelecionado() {
        Leilao selectedLeilao = leilaoTableView.getSelectionModel().getSelectedItem();
        btnAvaliar.setDisable(selectedLeilao == null || !selectedLeilao.getVendido());
    }

    @FXML
    void handleBtnMenu(ActionEvent event) {

    }


}
