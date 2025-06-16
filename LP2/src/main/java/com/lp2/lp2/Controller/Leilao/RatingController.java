package com.lp2.lp2.Controller.Leilao;

import com.lp2.lp2.DAO.LeilaoClassificacaoDAO;
import com.lp2.lp2.Model.LeilaoClassificacao;
import com.lp2.lp2.Session.Session;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.sql.Date;
import java.sql.SQLException;

public class RatingController {
    @FXML
    private ChoiceBox<Integer> ratingChoice;
    @FXML
    private TextArea commentField;
    @FXML
    private Button btnEnviar;

    private LeilaoClassificacaoDAO classificacaoDAO;
    private int leilaoId;

    public RatingController() throws SQLException {
        classificacaoDAO = new LeilaoClassificacaoDAO();
    }

    public void setLeilaoId(int leilaoId) {
        this.leilaoId = leilaoId;
    }

    @FXML
    private void initialize() {
        ratingChoice.setItems(FXCollections.observableArrayList(1,2,3,4,5));
        ratingChoice.getSelectionModel().selectFirst();
    }

    @FXML
    void handleBtnEnviar(ActionEvent event) {
        int rating = ratingChoice.getValue();
        String comentario = commentField.getText();
        int clienteId = Session.getLoggedUserId();

        try {
            if (classificacaoDAO.existsByClienteLeilao(clienteId, leilaoId)) {
                mostrarMensagemErro("Já avaliou este leilão.");
                return;
            }
            LeilaoClassificacao lc = new LeilaoClassificacao();
            lc.setClassificacao(rating);
            lc.setComentario(comentario);
            lc.setDataClassificacao(new Date(System.currentTimeMillis()));
            lc.setClienteId(clienteId);
            lc.setLeilaoId(leilaoId);
            classificacaoDAO.addClassificacao(lc);
            mostrarMensagemSucesso("Avaliação registada!");
            Stage stage = (Stage) btnEnviar.getScene().getWindow();
            stage.close();
        } catch (SQLException e) {
            mostrarMensagemErro("Erro ao guardar avaliação: " + e.getMessage());
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
}