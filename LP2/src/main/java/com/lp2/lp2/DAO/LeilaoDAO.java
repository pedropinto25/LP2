package com.lp2.lp2.DAO;

import com.lp2.lp2.DAO.IDAO.ILeilaoDAO;
import com.lp2.lp2.Model.Categoria;
import com.lp2.lp2.Model.Leilao;
import com.lp2.lp2.Infrastucture.Connection.DBConnection;
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


    private List<Categoria> getCategoriasByLeilaoId(int leilaoId) throws SQLException {
        List<Categoria> list = new ArrayList<>();
        String sql = "SELECT c.id, c.nome FROM Categoria c JOIN LeilaoCategoria lc ON c.id = lc.categoria_id WHERE lc.leilao_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, leilaoId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Categoria c = new Categoria();
                    c.setId(rs.getInt("id"));
                    c.setNome(rs.getString("nome"));
                    list.add(c);
                }
            }
        }
        return list;
    }

    private void insertLeilaoCategorias(int leilaoId, List<Categoria> categorias) throws SQLException {
        if (categorias == null) return;
        String sql = "INSERT INTO LeilaoCategoria (leilao_id, categoria_id) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            for (Categoria c : categorias) {
                stmt.setInt(1, leilaoId);
                stmt.setInt(2, c.getId());
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }

    private void deleteLeilaoCategorias(int leilaoId) throws SQLException {
        String sql = "DELETE FROM LeilaoCategoria WHERE leilao_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, leilaoId);
            stmt.executeUpdate();
        }
    }

    @Override
    public void addLeilao(Leilao leilao) throws SQLException {
        String sql = "INSERT INTO Leilao (nome, descricao, tipo, dataInicio, dataFim, valorMinimo, valorMaximo, multiploLance, inativo, vendido) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, leilao.getNome());
            stmt.setString(2, leilao.getDescricao());
            stmt.setString(3, leilao.getTipo());
            stmt.setDate(4, leilao.getDataInicio());
            stmt.setDate(5, leilao.getDataFim());
            stmt.setBigDecimal(6, leilao.getValorMinimo());
            stmt.setBigDecimal(7, leilao.getValorMaximo());
            if (leilao.getMultiploLance() != null) {
                stmt.setBigDecimal(8, leilao.getMultiploLance());
            } else {
                stmt.setNull(8, Types.DECIMAL);
            }
            stmt.setBoolean(9, leilao.getInativo());
            stmt.setBoolean(10, leilao.getVendido());
            stmt.executeUpdate();

            // Recuperar o ID gerado
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int generatedId = generatedKeys.getInt(1);
                    leilao.setId(generatedId); // Define o ID no objeto Leilao
                    insertLeilaoCategorias(generatedId, leilao.getCategorias());
                    // Salvar no CSV
                    try {
                        CsvService csvService = new CsvService();
                        csvService.saveLeilaoToCsv(leilao);
                    } catch (IOException e) {
                        System.err.println("Erro ao salvar leilão no CSV: " + e.getMessage());
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao adicionar leilão: " + e.getMessage());
            throw e; // Re-lançando a exceção para o chamador
        }
    }

    @Override
    public void updateLeilao(Leilao leilao) throws SQLException {
        String sql = "UPDATE Leilao SET nome = ?, descricao = ?, tipo = ?, dataInicio = ?, dataFim = ?, valorMinimo = ?, valorMaximo = ?, multiploLance = ?, inativo = ?, vendido = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, leilao.getNome());
            stmt.setString(2, leilao.getDescricao());
            stmt.setString(3, leilao.getTipo());
            //stmt.setDate(4, leilao.getDataInicio());
            //stmt.setDate(5, leilao.getDataFim());
            stmt.setDate(4, new java.sql.Date(leilao.getDataInicio().getTime()));
            stmt.setDate(5, new java.sql.Date(leilao.getDataFim().getTime()));

            stmt.setBigDecimal(6, leilao.getValorMinimo());
            stmt.setBigDecimal(7, leilao.getValorMaximo());
            if (leilao.getMultiploLance() != null) {
                stmt.setBigDecimal(8, leilao.getMultiploLance());
            } else {
                stmt.setNull(8, Types.DECIMAL);
            }
            stmt.setBoolean(9, leilao.getInativo());
            stmt.setBoolean(10, leilao.getVendido());
            stmt.setInt(11, leilao.getId());
            stmt.executeUpdate();
        }

        deleteLeilaoCategorias(leilao.getId());
        insertLeilaoCategorias(leilao.getId(), leilao.getCategorias());

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
        deleteLeilaoCategorias(id);
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
                    leilao.setInativo(rs.getBoolean("inativo"));
                    leilao.setVendido(rs.getBoolean("vendido"));
                    leilao.setCategorias(getCategoriasByLeilaoId(id));
                    return leilao;
                }
            }
        }
        return null;
    }

    @Override
    public List<Leilao> getAllLeiloes() throws SQLException {
        List<Leilao> leiloes = new ArrayList<>();
        String sql = "SELECT * FROM Leilao WHERE inativo = 0";
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
                leilao.setInativo(rs.getBoolean("inativo"));
                leilao.setVendido(rs.getBoolean("vendido"));
                leilao.setCategorias(getCategoriasByLeilaoId(leilao.getId()));
                leiloes.add(leilao);
            }
        }
        return leiloes;
    }
@Override
public List<Categoria> getCategoriasByLeilaoId1(int leilaoId) throws SQLException {
        List<Categoria> categorias = new ArrayList<>();
        String sql = "SELECT c.id, c.nome " +
                "FROM Categoria c " +
                "JOIN LeilaoCategoria lc ON c.id = lc.categoria_id " +
                "WHERE lc.leilao_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, leilaoId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Categoria categoria = new Categoria();
                    categoria.setId(rs.getInt("id"));
                    categoria.setNome(rs.getString("nome"));
                    categorias.add(categoria); // mesmo que seja só uma, mantemos como lista
                }
            }
        }
        return categorias;
    }


    public void desativarLeilao(int id) throws SQLException {
        String sql = "UPDATE Leilao SET inativo = 1 WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public void inserirLeiloes(List<Leilao> leiloes) throws SQLException {
        String sql = "INSERT INTO Leilao (nome, descricao, tipo, dataInicio, dataFim, valorMinimo, valorMaximo, multiploLance) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (Leilao l : leiloes) {
                stmt.setString(1, l.getNome());
                stmt.setString(2, l.getDescricao());
                stmt.setString(3, l.getTipo());
                stmt.setDate(4, l.getDataInicio());
                stmt.setDate(5, l.getDataFim());
                stmt.setBigDecimal(6, l.getValorMinimo());
                stmt.setBigDecimal(7, l.getValorMaximo());
                stmt.setBigDecimal(8, l.getMultiploLance());
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }
}