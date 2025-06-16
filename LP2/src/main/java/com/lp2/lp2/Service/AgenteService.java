package com.lp2.lp2.Service;

import com.lp2.lp2.DAO.AgenteDAO;
import com.lp2.lp2.DAO.LanceDAO;
import com.lp2.lp2.DAO.LeilaoDAO;
import com.lp2.lp2.DAO.PontosDAO;
import com.lp2.lp2.Model.Agente;
import com.lp2.lp2.Model.Lance;
import com.lp2.lp2.Model.Leilao;
import com.lp2.lp2.DAO.ClienteDAO;
import com.lp2.lp2.Model.Cliente;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public class AgenteService {
    private final AgenteDAO agenteDAO;
    private final LanceDAO lanceDAO;
    private final PontosDAO pontosDAO;
    private final LeilaoDAO leilaoDAO;
    private final ClienteDAO clienteDAO;
    private final EmailNotificationService emailService;

    public AgenteService() throws SQLException {
        agenteDAO = new AgenteDAO();
        lanceDAO = new LanceDAO();
        pontosDAO = new PontosDAO();
        leilaoDAO = new LeilaoDAO();
        clienteDAO = new ClienteDAO();
        emailService = new EmailNotificationService();
    }

    public void processarTodasAutomacoes() {
        processarTodasAutomacoesSequencial();
    }

    public void processarTodasAutomacoesSequencial() {
        new Thread(() -> {
            try {
                List<Integer> leiloesComAgentes = agenteDAO.getLeiloesComAgentes();
                for (Integer leilaoId : leiloesComAgentes) {
                    System.out.println("Processando leilão ID: " + leilaoId);
                    executar(leilaoId); // processo normal de automação
                    desativarAgentesDoLeilao(leilaoId); // marca como desativado
                    Thread.sleep(15000); // espera 15 segundos entre leilões
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void executar(int leilaoId) throws SQLException, InterruptedException {
        List<Agente> agentes = agenteDAO.getAgentesByLeilao(leilaoId);
        if (agentes.isEmpty()) return;

        BigDecimal valorAtual = obterUltimoValor(leilaoId);
        for (Agente ag : agentes) {
            int pontos = pontosDAO.verificarPontos(ag.getClienteId());
            if (pontos < ag.getIncremento().intValue()) {
                System.out.println("Cliente " + ag.getClienteId() + " sem créditos para o agente.");
                Cliente cli = clienteDAO.getClienteById(ag.getClienteId());
                if (cli != null) {
                    emailService.enviarSemCreditos(cli.getEmail(), cli.getNome());
                }
                continue;
            }
            valorAtual = valorAtual.add(ag.getIncremento());
            Lance lance = new Lance();
            lance.setClienteId(ag.getClienteId());
            lance.setLeilaoId(leilaoId);
            lance.setValor(valorAtual);
            lance.setDataHora(new Timestamp(System.currentTimeMillis()));
            lanceDAO.addLance(lance);
            pontosDAO.removerPontos(ag.getClienteId(), ag.getIncremento().intValue());

            Leilao leilao = leilaoDAO.getLeilaoById(leilaoId);
            if (leilao.getDataFim() != null) {
                long diff = leilao.getDataFim().getTime() - System.currentTimeMillis();
                if (diff <= 15000) {
                    leilao.setDataFim(new java.util.Date(leilao.getDataFim().getTime() + 15000));
                    leilaoDAO.updateLeilao(leilao);
                }
            }
            Thread.sleep(5000);
        }
    }

    private BigDecimal obterUltimoValor(int leilaoId) throws SQLException {
        String sql = "SELECT TOP 1 valor FROM Lance WHERE leilaoId = ? ORDER BY id DESC";
        try (var conn = com.lp2.lp2.Infrastucture.Connection.DBConnection.getConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, leilaoId);
            try (var rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getBigDecimal(1);
                }
            }
        }
        Leilao leilao = leilaoDAO.getLeilaoById(leilaoId);
        return leilao.getValorMinimo() != null ? leilao.getValorMinimo() : BigDecimal.ZERO;
    }

    public void desativarAgentesDoLeilao(int leilaoId) throws SQLException {
        String sql = "UPDATE Agente SET ativo = 0 WHERE leilaoId = ?";
        try (var conn = com.lp2.lp2.Infrastucture.Connection.DBConnection.getConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, leilaoId);
            stmt.executeUpdate();
        }
    }
}
