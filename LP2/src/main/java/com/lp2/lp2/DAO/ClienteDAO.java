package com.lp2.lp2.DAO;

import com.lp2.lp2.DAO.IDAO.IClienteDAO;
import com.lp2.lp2.Model.Cliente;
import com.lp2.lp2.Infrastucture.Connection.DBConnection ;
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
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getMorada());
            stmt.setDate(3, cliente.getDataNascimento());
            stmt.setString(4, cliente.getEmail());
            stmt.setString(5, cliente.getSenha());
            stmt.executeUpdate();
        }
    }

    @Override
    public void updateCliente(Cliente cliente) throws SQLException {
        String sql = "UPDATE Cliente SET nome = ?, morada = ?, dataNascimento = ?, email = ?, senha = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getMorada());
            stmt.setDate(3, cliente.getDataNascimento());
            stmt.setString(4, cliente.getEmail());
            stmt.setString(5, cliente.getSenha());
            stmt.setInt(6, cliente.getId());
            stmt.executeUpdate();
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