package com.lp2.lp2.DAO;

import com.lp2.lp2.DAO.IDAO.ILeilaoParticipacaoDAO;
import com.lp2.lp2.Model.LeilaoParticipacao;
import com.lp2.lp2.Infrastucture.Connection.DBConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LeilaoParticipacaoDAO implements ILeilaoParticipacaoDAO {
    private Connection connection;

    public LeilaoParticipacaoDAO() throws SQLException {
        connection = DBConnection.getConnection();
    }

    @Override
    public void addParticipacao(LeilaoParticipacao participacao) throws SQLException {
        String sql = "INSERT INTO LeilaoParticipacao (leilao_id, cliente_id, valor_lance) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, participacao.getLeilaoId());
            stmt.setInt(2, participacao.getClienteId());
            stmt.setBigDecimal(3, participacao.getValorLance());
            stmt.executeUpdate();

            // Recuperar o ID gerado
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int generatedId = generatedKeys.getInt(1);
                    participacao.setId(generatedId); // Define o ID no objeto LeilaoParticipacao
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao adicionar participação: " + e.getMessage());
            throw e; // Re-lançando a exceção para o chamador
        }
    }

    @Override
    public void updateParticipacao(LeilaoParticipacao participacao) throws SQLException {
        String sql = "UPDATE LeilaoParticipacao SET leilao_id = ?, cliente_id = ?, data_participacao = ?, valor_lance = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, participacao.getLeilaoId());
            stmt.setInt(2, participacao.getClienteId());
            stmt.setTimestamp(3, participacao.getDataParticipacao());
            stmt.setBigDecimal(4, participacao.getValorLance());
            stmt.setInt(5, participacao.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar participação: " + e.getMessage());
            throw e; // Re-lançando a exceção para o chamador
        }
    }

    @Override
    public void deleteParticipacao(int id) throws SQLException {
        String sql = "DELETE FROM LeilaoParticipacao WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao deletar participação: " + e.getMessage());
            throw e; // Re-lançando a exceção para o chamador
        }
    }

    @Override
    public LeilaoParticipacao getParticipacaoById(int id) throws SQLException {
        String sql = "SELECT * FROM LeilaoParticipacao WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    LeilaoParticipacao participacao = new LeilaoParticipacao();
                    participacao.setId(rs.getInt("id"));
                    participacao.setLeilaoId(rs.getInt("leilao_id"));
                    participacao.setClienteId(rs.getInt("cliente_id"));
                    participacao.setDataParticipacao(rs.getTimestamp("data_participacao"));
                    participacao.setValorLance(rs.getBigDecimal("valor_lance"));
                    return participacao;
                }
            }
        }
        return null;
    }

    @Override
    public List<LeilaoParticipacao> getParticipacoesByLeilaoId(int leilaoId) throws SQLException {
        List<LeilaoParticipacao> participacoes = new ArrayList<>();
        String sql = "SELECT * FROM LeilaoParticipacao WHERE leilao_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, leilaoId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    LeilaoParticipacao participacao = new LeilaoParticipacao();
                    participacao.setId(rs.getInt("id"));
                    participacao.setLeilaoId(rs.getInt("leilao_id"));
                    participacao.setClienteId(rs.getInt("cliente_id"));
                    participacao.setDataParticipacao(rs.getTimestamp("data_participacao"));
                    participacao.setValorLance(rs.getBigDecimal("valor_lance"));
                    participacoes.add(participacao);
                }
            }
        }
        return participacoes;
    }

    @Override
    public List<LeilaoParticipacao> getAllParticipacoes() throws SQLException {
        List<LeilaoParticipacao> participacoes = new ArrayList<>();
        String sql = "SELECT * FROM LeilaoParticipacao";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                LeilaoParticipacao participacao = new LeilaoParticipacao();
                participacao.setId(rs.getInt("id"));
                participacao.setLeilaoId(rs.getInt("leilao_id"));
                participacao.setClienteId(rs.getInt("cliente_id"));
                participacao.setDataParticipacao(rs.getTimestamp("data_participacao"));
                participacao.setValorLance(rs.getBigDecimal("valor_lance"));
                participacoes.add(participacao);
            }
        }
        return participacoes;
    }

    @Override
    public List<LeilaoParticipacao> getClientesComMaisLancesPorLeilao() {
        List<LeilaoParticipacao> clientesLances = new ArrayList<>();
        String sql = "SELECT lp.leilao_id, lp.cliente_id, COUNT(lp.id) AS total_lances, MAX(lp.valor_lance) AS maior_lance " +
                "FROM LeilaoParticipacao lp " +
                "GROUP BY lp.leilao_id, lp.cliente_id " +
                "ORDER BY lp.leilao_id, maior_lance DESC";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int leilaoId = rs.getInt("leilao_id");
                int clienteId = rs.getInt("cliente_id");
                BigDecimal maiorLance = rs.getBigDecimal("maior_lance");

                LeilaoParticipacao clienteLances = new LeilaoParticipacao();
                clienteLances.setLeilaoId(leilaoId);
                clienteLances.setClienteId(clienteId);
                clienteLances.setValorLance(maiorLance);

                clientesLances.add(clienteLances);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return clientesLances;
    }
}