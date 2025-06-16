package com.lp2.lp2.DAO;

import com.lp2.lp2.DAO.IDAO.IAgenteDAO;
import com.lp2.lp2.Infrastucture.Connection.DBConnection;
import com.lp2.lp2.Model.Agente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AgenteDAO implements IAgenteDAO {
    private final Connection connection;

    public AgenteDAO() throws SQLException {
        this.connection = DBConnection.getConnection();
    }

    @Override
    public void addAgente(Agente agente) throws SQLException {
        String sqlOrdem = "SELECT COALESCE(MAX(ordem),0)+1 FROM Agente WHERE leilaoId = ?";
        try (PreparedStatement ordStmt = connection.prepareStatement(sqlOrdem)) {
            ordStmt.setInt(1, agente.getLeilaoId());
            try (ResultSet rs = ordStmt.executeQuery()) {
                if (rs.next()) {
                    agente.setOrdem(rs.getInt(1));
                }
            }
        }
        String sql = "INSERT INTO Agente (clienteId, leilaoId, ordem, incremento, limite, ativo) VALUES (?,?,?,?,?,?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, agente.getClienteId());
            stmt.setInt(2, agente.getLeilaoId());
            stmt.setInt(3, agente.getOrdem());
            stmt.setBigDecimal(4, agente.getIncremento());
            stmt.setBigDecimal(5, agente.getLimite());
            stmt.setBoolean(6, agente.isAtivo());
            stmt.executeUpdate();
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    agente.setId(keys.getInt(1));
                }
            }
        }
    }

    @Override
    public void updateAgente(Agente agente) throws SQLException {
        String sql = "UPDATE Agente SET clienteId=?, leilaoId=?, ordem=?, incremento=?, limite=?, ativo=? WHERE id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, agente.getClienteId());
            stmt.setInt(2, agente.getLeilaoId());
            stmt.setInt(3, agente.getOrdem());
            stmt.setBigDecimal(4, agente.getIncremento());
            stmt.setBigDecimal(5, agente.getLimite());
            stmt.setBoolean(6, agente.isAtivo());
            stmt.setInt(7, agente.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void deleteAgente(int id) throws SQLException {
        String sql = "DELETE FROM Agente WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public Agente getAgenteById(int id) throws SQLException {
        String sql = "SELECT * FROM Agente WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapAgente(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<Agente> getAgentesByLeilao(int leilaoId) throws SQLException {
        String sql = "SELECT * FROM Agente WHERE leilaoId = ? AND ativo = 1 ORDER BY ordem";
        List<Agente> list = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, leilaoId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapAgente(rs));
                }
            }
        }
        return list;
    }

    @Override
    public List<Agente> getAllAgentes() throws SQLException {
        String sql = "SELECT * FROM Agente";
        List<Agente> list = new ArrayList<>();
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapAgente(rs));
            }
        }
        return list;
    }

    private Agente mapAgente(ResultSet rs) throws SQLException {
        Agente a = new Agente();
        a.setId(rs.getInt("id"));
        a.setClienteId(rs.getInt("clienteId"));
        a.setLeilaoId(rs.getInt("leilaoId"));
        a.setOrdem(rs.getInt("ordem"));
        a.setIncremento(rs.getBigDecimal("incremento"));
        a.setLimite(rs.getBigDecimal("limite"));
        a.setAtivo(rs.getBoolean("ativo"));
        return a;
    }
}