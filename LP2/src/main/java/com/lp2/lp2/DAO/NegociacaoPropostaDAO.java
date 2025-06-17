package com.lp2.lp2.DAO;

import com.lp2.lp2.DAO.IDAO.INegociacaoPropostaDAO;
import com.lp2.lp2.Infrastucture.Connection.DBConnection;
import com.lp2.lp2.Model.NegociacaoProposta;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NegociacaoPropostaDAO implements INegociacaoPropostaDAO {
    private final Connection connection;

    public NegociacaoPropostaDAO() throws SQLException {
        this.connection = DBConnection.getConnection();
    }

    @Override
    public void addProposta(NegociacaoProposta proposta) throws SQLException {
        String sql = "INSERT INTO NegociacaoProposta (leilao_id, cliente_id, valor, estado, data) VALUES (?,?,?,?,?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, proposta.getLeilaoId());
            stmt.setInt(2, proposta.getClienteId());
            stmt.setBigDecimal(3, proposta.getValor());
            stmt.setString(4, proposta.getEstado());
            stmt.setTimestamp(5, proposta.getData());
            stmt.executeUpdate();
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) proposta.setId(keys.getInt(1));
            }
        }
    }

    @Override
    public void updateProposta(NegociacaoProposta proposta) throws SQLException {
        String sql = "UPDATE NegociacaoProposta SET valor=?, estado=?, data=? WHERE id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setBigDecimal(1, proposta.getValor());
            stmt.setString(2, proposta.getEstado());
            stmt.setTimestamp(3, proposta.getData());
            stmt.setInt(4, proposta.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public List<NegociacaoProposta> getPropostasByLeilao(int leilaoId) throws SQLException {
        String sql = "SELECT * FROM NegociacaoProposta WHERE leilao_id = ?";
        List<NegociacaoProposta> list = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, leilaoId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    NegociacaoProposta p = new NegociacaoProposta();
                    p.setId(rs.getInt("id"));
                    p.setLeilaoId(rs.getInt("leilao_id"));
                    p.setClienteId(rs.getInt("cliente_id"));
                    p.setValor(rs.getBigDecimal("valor"));
                    p.setEstado(rs.getString("estado"));
                    p.setData(rs.getTimestamp("data"));
                    list.add(p);
                }
            }
        }
        return list;
    }
}