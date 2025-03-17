package com.lp2.lp2.DAO;

import com.lp2.lp2.DAO.IDAO.ILeilaoDAO;
import com.lp2.lp2.Model.Leilao;
import com.lp2.lp2.Infrastucture.Connection.DBConnection ;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class LeilaoDAO implements ILeilaoDAO {
    private Connection connection;

    public LeilaoDAO() throws SQLException {
        connection = DBConnection.getConnection();
    }

    @Override
    public void addLeilao(Leilao leilao) throws SQLException {
        String sql = "INSERT INTO Leilao (nome, descricao, tipo, dataInicio, dataFim, valorMinimo, valorMaximo, multiploLance) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, leilao.getNome());
            stmt.setString(2, leilao.getDescricao());
            stmt.setString(3, leilao.getTipo());
            stmt.setDate(4, leilao.getDataInicio());
            stmt.setDate(5, leilao.getDataFim());
            stmt.setBigDecimal(6, leilao.getValorMinimo());
            stmt.setBigDecimal(7, leilao.getValorMaximo());
            stmt.setBigDecimal(8, leilao.getMultiploLance());
            stmt.executeUpdate();
        }
    }

    @Override
    public void updateLeilao(Leilao leilao) throws SQLException {
        String sql = "UPDATE Leilao SET nome = ?, descricao = ?, tipo = ?, dataInicio = ?, dataFim = ?, valorMinimo = ?, valorMaximo = ?, multiploLance = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, leilao.getNome());
            stmt.setString(2, leilao.getDescricao());
            stmt.setString(3, leilao.getTipo());
            stmt.setDate(4, leilao.getDataInicio());
            stmt.setDate(5, leilao.getDataFim());
            stmt.setBigDecimal(6, leilao.getValorMinimo());
            stmt.setBigDecimal(7, leilao.getValorMaximo());
            stmt.setBigDecimal(8, leilao.getMultiploLance());
            stmt.setInt(9, leilao.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void deleteLeilao(int id) throws SQLException {
        String sql = "DELETE FROM Leilao WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public Leilao getLeilaoById(int id) throws SQLException {
        String sql = "SELECT * FROM Leilao WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Leilao leilao = new Leilao();
                    leilao.setId(rs.getInt("id"));
                    leilao.setNome(rs.getString("nome"));
                    leilao.setDescricao(rs.getString("descricao"));
                    leilao.setTipo(rs.getString("tipo"));
                    leilao.setDataInicio(rs.getDate("dataInicio"));
                    leilao.setDataFim(rs.getDate("dataFim"));
                    leilao.setValorMinimo(rs.getBigDecimal("valorMinimo"));
                    leilao.setValorMaximo(rs.getBigDecimal("valorMaximo"));
                    leilao.setMultiploLance(rs.getBigDecimal("multiploLance"));
                    return leilao;
                }
            }
        }
        return null;
    }

    @Override
    public List<Leilao> getAllLeiloes() throws SQLException {
        List<Leilao> leiloes = new ArrayList<>();
        String sql = "SELECT * FROM Leilao";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Leilao leilao = new Leilao();
                leilao.setId(rs.getInt("id"));
                leilao.setNome(rs.getString("nome"));
                leilao.setDescricao(rs.getString("descricao"));
                leilao.setTipo(rs.getString("tipo"));
                leilao.setDataInicio(rs.getDate("dataInicio"));
                leilao.setDataFim(rs.getDate("dataFim"));
                leilao.setValorMinimo(rs.getBigDecimal("valorMinimo"));
                leilao.setValorMaximo(rs.getBigDecimal("valorMaximo"));
                leilao.setMultiploLance(rs.getBigDecimal("multiploLance"));
                leiloes.add(leilao);
            }
        }
        return leiloes;
    }
}