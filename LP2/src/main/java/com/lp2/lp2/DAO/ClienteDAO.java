package com.lp2.lp2.DAO;

import com.lp2.lp2.DAO.IDAO.IClienteDAO;
import com.lp2.lp2.Model.Cliente;
import com.lp2.lp2.Infrastucture.Connection.DBConnection ;
import com.lp2.lp2.Util.CsvService;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class ClienteDAO implements IClienteDAO {
    private Connection connection;

    public ClienteDAO() throws SQLException {
        connection = DBConnection.getConnection();
    }

    @Override
    public void addCliente(Cliente cliente) throws SQLException {
        String sql = "INSERT INTO Cliente (nome, morada, dataNascimento, email, senha) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getMorada());
            stmt.setDate(3, cliente.getDataNascimento());
            stmt.setString(4, cliente.getEmail());
            stmt.setString(5, cliente.getSenha());
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    cliente.setId(id);

                    // Chama o serviço para salvar os dados no CSV
                    CsvService csvService = new CsvService();
                    try {
                        csvService.saveClienteToCsv(cliente);
                    } catch (IOException e) {
                        System.err.println("Erro ao salvar cliente no CSV: " + e.getMessage());
                    }
                } else {
                    throw new SQLException("Falha ao obter o ID do cliente inserido.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao adicionar cliente: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public void updateCliente(Cliente cliente) throws SQLException {
        String sql = "UPDATE Cliente SET nome = ?, morada = ?, dataNascimento = ?, email = ?, senha = ?, encrypted = 0 WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getMorada());
            stmt.setDate(3, cliente.getDataNascimento());
            stmt.setString(4, cliente.getEmail());
            stmt.setString(5, cliente.getSenha());
            stmt.setInt(6, cliente.getId());
            stmt.executeUpdate();
        }

        String sqlUpdateUsers = "UPDATE Users SET email = ?, password_hash = ?, encrypted = 0 WHERE id = ?";
        try (PreparedStatement stmtUsers = connection.prepareStatement(sqlUpdateUsers)) {
            stmtUsers.setString(1, cliente.getEmail());
            stmtUsers.setString(2, cliente.getSenha());
            stmtUsers.setInt(3, cliente.getId());
            stmtUsers.executeUpdate();
        }

        // Atualizar também no CSV
        try {
            CsvService csvService = new CsvService();
            csvService.updateClienteInCsv(cliente);
        } catch (Exception e) {
            System.err.println("Erro ao atualizar cliente no CSV: " + e.getMessage());
        }
    }


    @Override
    public void deleteCliente(int id) throws SQLException {
        String sql = "DELETE FROM Cliente WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public Cliente getClienteById(int id) throws SQLException {
        String sql = "SELECT * FROM Cliente WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Cliente cliente = new Cliente();
                    cliente.setId(rs.getInt("id"));
                    cliente.setNome(rs.getString("nome"));
                    cliente.setMorada(rs.getString("morada"));
                    cliente.setDataNascimento(rs.getDate("dataNascimento"));
                    cliente.setEmail(rs.getString("email"));
                    cliente.setSenha(rs.getString("senha"));
                    return cliente;
                }
            }
        }
        return null;
    }

    @Override
    public List<Cliente> getAllClientes() throws SQLException {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM Cliente";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Cliente cliente = new Cliente();
                cliente.setId(rs.getInt("id"));
                cliente.setNome(rs.getString("nome"));
                cliente.setMorada(rs.getString("morada"));
                cliente.setDataNascimento(rs.getDate("dataNascimento"));
                cliente.setEmail(rs.getString("email"));
                cliente.setSenha(rs.getString("senha"));
                clientes.add(cliente);
            }
        }
        return clientes;
    }
}