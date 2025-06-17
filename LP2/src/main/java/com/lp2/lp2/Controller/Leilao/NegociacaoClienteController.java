package com.lp2.lp2.Controller.Leilao;

import com.lp2.lp2.DAO.NegociacaoPropostaDAO;
import com.lp2.lp2.Model.NegociacaoProposta;
import com.lp2.lp2.Service.EmailNotificationService;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;

public class NegociacaoClienteController {
    @FXML private TextField valorField;
    private NegociacaoPropostaDAO dao;
    private EmailNotificationService emailService = new EmailNotificationService();

    public NegociacaoClienteController() throws SQLException {
        dao = new NegociacaoPropostaDAO();
    }

    @FXML
    private void handlePropor() {
        try {
            NegociacaoProposta p = new NegociacaoProposta();
            p.setLeilaoId(0); // placeholder
            p.setClienteId(0); // placeholder
            p.setValor(new BigDecimal(valorField.getText()));
            p.setEstado("Pendente");
            p.setData(new Timestamp(System.currentTimeMillis()));
            dao.addProposta(p);
            emailService.enviarSemCreditos("pintop2003@gmail.com","Nova Proposta");
            close();
        } catch (Exception e) { e.printStackTrace(); }
    }

    @FXML
    private void handleAceitar() {
        handlePropor();
    }

    private void close() {
        Stage stage = (Stage) valorField.getScene().getWindow();
        stage.close();
    }
}