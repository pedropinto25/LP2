package com.lp2.lp2.Controller.Categoria;

import com.lp2.lp2.DAO.CategoriaDAO;
import com.lp2.lp2.Model.Categoria;
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

import java.sql.SQLException;

public class CategoriaController {
    @FXML
    private TableView<Categoria> categoriaTable;
    @FXML
    private TableColumn<Categoria, Integer> idColumn;
    @FXML
    private TableColumn<Categoria, String> nomeColumn;
    @FXML
    private TextField nomeField;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnDelete;

    private CategoriaDAO categoriaDAO;

    public CategoriaController() throws SQLException {
        categoriaDAO = new CategoriaDAO();
    }

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomeColumn.setCellValueFactory(new PropertyValueFactory<>("nome"));
        loadCategorias();
    }

    private void loadCategorias() {
        try {
            ObservableList<Categoria> list = FXCollections.observableArrayList(categoriaDAO.getAllCategorias());
            categoriaTable.setItems(list);
        } catch (SQLException e) {
            showError(e.getMessage());
        }
    }

    @FXML
    void handleAdd(ActionEvent event) {
        String nome = nomeField.getText();
        if (nome.isEmpty()) return;
        try {
            Categoria c = new Categoria(nome);
            categoriaDAO.addCategoria(c);
            nomeField.clear();
            loadCategorias();
        } catch (SQLException e) {
            showError(e.getMessage());
        }
    }

    @FXML
    void handleDelete(ActionEvent event) {
        Categoria c = categoriaTable.getSelectionModel().getSelectedItem();
        if (c == null) return;
        try {
            categoriaDAO.deleteCategoria(c.getId());
            loadCategorias();
        } catch (SQLException e) {
            showError(e.getMessage());
        }
    }

    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}