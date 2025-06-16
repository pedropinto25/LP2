package com.lp2.lp2.DAO;

import com.lp2.lp2.DAO.IDAO.ILeilaoClassificacaoDAO;
import com.lp2.lp2.Model.LeilaoClassificacao;
import com.lp2.lp2.Infrastucture.Connection.DBConnection;

import java.sql.*;
import java.util.*;

public class LeilaoClassificacaoDAO implements ILeilaoClassificacaoDAO {
    private Connection connection;

    public LeilaoClassificacaoDAO() throws SQLException {
        connection = DBConnection.getConnection();
    }

    @Override
    public void addClassificacao(LeilaoClassificacao classificacao) throws SQLException {
        String sql = "INSERT INTO LeilaoClassificacao (classificacao, comentario, data_classificacao, cliente_id, leilao_id) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, classificacao.getClassificacao());
            stmt.setString(2, classificacao.getComentario());
            stmt.setDate(3, classificacao.getDataClassificacao());
            stmt.setInt(4, classificacao.getClienteId());
            stmt.setInt(5, classificacao.getLeilaoId());
            stmt.executeUpdate();
        }
    }

    @Override
    public List<LeilaoClassificacao> getClassificacoesByLeilaoId(int leilaoId) throws SQLException {
        List<LeilaoClassificacao> list = new ArrayList<>();
        String sql = "SELECT * FROM LeilaoClassificacao WHERE leilao_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, leilaoId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    LeilaoClassificacao lc = new LeilaoClassificacao();
                    lc.setId(rs.getInt("id"));
                    lc.setClassificacao(rs.getInt("classificacao"));
                    lc.setComentario(rs.getString("comentario"));
                    lc.setDataClassificacao(rs.getDate("data_classificacao"));
                    lc.setClienteId(rs.getInt("cliente_id"));
                    lc.setLeilaoId(rs.getInt("leilao_id"));
                    list.add(lc);
                }
            }
        }
        return list;
    }

    @Override
    public boolean existsByClienteLeilao(int clienteId, int leilaoId) throws SQLException {
        String sql = "SELECT 1 FROM LeilaoClassificacao WHERE cliente_id = ? AND leilao_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, clienteId);
            stmt.setInt(2, leilaoId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    @Override
    public Double getAverageRatingForLeilao(int leilaoId) throws SQLException {
        String sql = "SELECT AVG(classificacao) AS media FROM LeilaoClassificacao WHERE leilao_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, leilaoId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("media");
                }
            }
        }
        return null;
    }

    @Override
    public Double getAverageRatingOverall() throws SQLException {
        String sql = "SELECT AVG(classificacao) AS media FROM LeilaoClassificacao";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getDouble("media");
            }
        }
        return null;
    }

    @Override
    public Map<Integer, Double> getAverageRatingPerLeilao() throws SQLException {
        Map<Integer, Double> map = new HashMap<>();
        String sql = "SELECT leilao_id, AVG(classificacao) AS media FROM LeilaoClassificacao GROUP BY leilao_id";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                map.put(rs.getInt("leilao_id"), rs.getDouble("media"));
            }
        }
        return map;
    }
}
