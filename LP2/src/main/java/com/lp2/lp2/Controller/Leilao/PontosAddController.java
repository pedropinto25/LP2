package com.lp2.lp2.Controller.Leilao;

import com.lp2.lp2.DAO.PontosDAO;
import com.lp2.lp2.Session.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;

public class PontosAddController {

    @FXML
    private TextField pontosField;

    private PontosDAO pontosDAO;

    public PontosAddController() throws SQLException {
        pontosDAO = new PontosDAO();
    }

    @FXML
    void handleBtnAdicionar(ActionEvent event) {
        try {
            int pontos = Integer.parseInt(pontosField.getText());
            pontosDAO.pedirPontos(Session.getLoggedUserId(), pontos); // <--- NOVO MÉTODO!
            mostrarMensagemSucesso("Pedido de pontos enviado para aprovação!");

            // Fechar a janela
            Stage stage = (Stage) pontosField.getScene().getWindow();
            stage.close();
        } catch (SQLException e) {
            mostrarMensagemErro("Erro ao pedir pontos: " + e.getMessage());
        } catch (NumberFormatException e) {
            mostrarMensagemErro("Quantidade de pontos inválida.");
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
