package com.lp2.lp2.DAO;

import com.lp2.lp2.DAO.IDAO.ILeilaoDAO;
import com.lp2.lp2.Model.Leilao;
import com.lp2.lp2.Infrastucture.Connection.DBConnection ;
import com.lp2.lp2.Util.CsvService;
import com.opencsv.exceptions.CsvException;

import java.io.IOException;
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
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, leilao.getNome());
            stmt.setString(2, leilao.getDescricao());
            stmt.setString(3, leilao.getTipo());
            stmt.setDate(4, leilao.getDataInicio());
            stmt.setDate(5, leilao.getDataFim());
            stmt.setBigDecimal(6, leilao.getValorMinimo());
            stmt.setBigDecimal(7, leilao.getValorMaximo());
            stmt.setBigDecimal(8, leilao.getMultiploLance());
            stmt.executeUpdate();

            // Recuperar o ID gerado
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int generatedId = generatedKeys.getInt(1);
                    leilao.setId(generatedId); // Define o ID no objeto Leilao

                    // Salvar no CSV
                    try {
                        CsvService csvService = new CsvService();
                        csvService.saveLeilaoToCsv(leilao);
                    } catch (IOException e) {
                        System.err.println("Erro ao salvar leilão no CSV: " + e.getMessage());
                    }
                }
            }
        }
        catch (SQLException e) {
            System.err.println("Erro ao adicionar leilão: " + e.getMessage());
            throw e; // Re-lançando a exceção para o chamador
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

        // Atualizar no CSV
        try {
            CsvService csvService = new CsvService();
            csvService.updateLeilaoInCsv(leilao);
        } catch (IOException | CsvException e) {
            System.err.println("Erro ao atualizar leilão no CSV: " + e.getMessage());
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