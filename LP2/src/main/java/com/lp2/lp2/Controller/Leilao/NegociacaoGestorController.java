package com.lp2.lp2.Controller.Leilao;

import com.lp2.lp2.DAO.NegociacaoPropostaDAO;
import com.lp2.lp2.Model.NegociacaoProposta;
import com.lp2.lp2.Service.EmailNotificationService;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.List;

public class NegociacaoGestorController {
    @FXML private TableView<NegociacaoProposta> propostasTable;
    private NegociacaoPropostaDAO dao;
    private EmailNotificationService emailService = new EmailNotificationService();

    public NegociacaoGestorController() throws SQLException {
        dao = new NegociacaoPropostaDAO();
    }

    @FXML
    public void initialize() {
        try {
            List<NegociacaoProposta> list = dao.getPropostasByLeilao(0); // placeholder
            propostasTable.getItems().setAll(list);
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @FXML
    private void handleAceitar() { responder("Aceite"); }
    @FXML
    private void handleRecusar() { responder("Recusada"); }
    @FXML
    private void handleRenegociar() { responder("Renegociar"); }

    private void responder(String estado) {
        NegociacaoProposta p = propostasTable.getSelectionModel().getSelectedItem();
        if (p == null) return;
        try {
            p.setEstado(estado);
            dao.updateProposta(p);
            emailService.enviarSemCreditos("pintop2003@gmail.com",estado);
            propostasTable.refresh();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}