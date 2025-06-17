package com.lp2.lp2.Controller.Leilao;

import com.lp2.lp2.DAO.LeilaoDAO;
import com.lp2.lp2.Model.Categoria;
import com.lp2.lp2.Model.Leilao;
import com.lp2.lp2.Util.LoaderFXML;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.List;

public class ListarLeilaoClienteController {

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
    private TableColumn<Leilao, String> categoriasColumn;
    @FXML
    private TableColumn<Leilao, Boolean> inativoColumn;
    @FXML
    private TableColumn<Leilao, Boolean> vendidoColumn;

    @FXML
    private Button btnBack;
    @FXML
    private TextField searchField;

    private LeilaoDAO leilaoDAO;

    public ListarLeilaoClienteController() throws SQLException {
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
        inativoColumn.setCellValueFactory(new PropertyValueFactory<>("inativo"));
        vendidoColumn.setCellValueFactory(new PropertyValueFactory<>("vendido"));
        categoriasColumn.setCellValueFactory(cellData -> {
            List<Categoria> categorias = cellData.getValue().getCategorias();
            String nome = (!categorias.isEmpty()) ? categorias.get(0).getNome() : "";
            return new SimpleStringProperty(nome);
        });

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
        loader.loadMenuCliente();
    }

    private void mostrarMensagemErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
