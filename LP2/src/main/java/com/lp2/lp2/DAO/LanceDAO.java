package com.lp2.lp2.DAO;


import com.lp2.lp2.DAO.IDAO.ILanceDAO;
import com.lp2.lp2.Model.Lance;
import com.lp2.lp2.Infrastucture.Connection.DBConnection ;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class LanceDAO implements ILanceDAO {
    private Connection connection;

    public LanceDAO() throws SQLException {
        connection = DBConnection.getConnection();
    }

    @Override
    public void addLance(Lance lance) throws SQLException {
        String sql = "INSERT INTO Lance (valor, dataHora, clienteId, leilaoId) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setBigDecimal(1, lance.getValor());
            stmt.setTimestamp(2, lance.getDataHora());
            stmt.setInt(3, lance.getClienteId());
            stmt.setInt(4, lance.getLeilaoId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void updateLance(Lance lance) throws SQLException {
        String sql = "UPDATE Lance SET valor = ?, dataHora = ?, clienteId = ?, leilaoId = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setBigDecimal(1, lance.getValor());
            stmt.setTimestamp(2, lance.getDataHora());
            stmt.setInt(3, lance.getClienteId());
            stmt.setInt(4, lance.getLeilaoId());
            stmt.setInt(5, lance.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void deleteLance(int id) throws SQLException {
        String sql = "DELETE FROM Lance WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public Lance getLanceById(int id) throws SQLException {
        String sql = "SELECT * FROM Lance WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Lance lance = new Lance();
                    lance.setId(rs.getInt("id"));
                    lance.setValor(rs.getBigDecimal("valor"));
                    lance.setDataHora(rs.getTimestamp("dataHora"));
                    lance.setClienteId(rs.getInt("clienteId"));
                    lance.setLeilaoId(rs.getInt("leilaoId"));
                    return lance;
                }
            }
        }
        return null;
    }

    @Override
    public List<Lance> getAllLances() throws SQLException {
        List<Lance> lances = new ArrayList<>();
        String sql = "SELECT * FROM Lance";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Lance lance = new Lance();
                lance.setId(rs.getInt("id"));
                lance.setValor(rs.getBigDecimal("valor"));
                lance.setDataHora(rs.getTimestamp("dataHora"));
                lance.setClienteId(rs.getInt("clienteId"));
                lance.setLeilaoId(rs.getInt("leilaoId"));
                lances.add(lance);
            }
        }
        return lances;
    }
}