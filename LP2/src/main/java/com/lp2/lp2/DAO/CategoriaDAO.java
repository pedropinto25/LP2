package com.lp2.lp2.DAO;

import com.lp2.lp2.DAO.IDAO.ICategoriaDAO;
import com.lp2.lp2.Infrastucture.Connection.DBConnection;
import com.lp2.lp2.Model.Categoria;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO implements ICategoriaDAO {
    private Connection connection;

    public CategoriaDAO() throws SQLException {
        connection = DBConnection.getConnection();
    }

    @Override
    public void addCategoria(Categoria categoria) throws SQLException {
        String sql = "INSERT INTO Categoria (nome) VALUES (?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, categoria.getNome());
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    categoria.setId(rs.getInt(1));
                }
            }
        }
    }

    @Override
    public void updateCategoria(Categoria categoria) throws SQLException {
        String sql = "UPDATE Categoria SET nome = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, categoria.getNome());
            stmt.setInt(2, categoria.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void deleteCategoria(int id) throws SQLException {
        String check = "SELECT COUNT(*) FROM LeilaoCategoria WHERE categoria_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(check)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    throw new SQLException("Categoria associada a leiloes");
                }
            }
        }
        String sql = "DELETE FROM Categoria WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public Categoria getCategoriaById(int id) throws SQLException {
        String sql = "SELECT * FROM Categoria WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Categoria c = new Categoria();
                    c.setId(rs.getInt("id"));
                    c.setNome(rs.getString("nome"));
                    return c;
                }
            }
        }
        return null;
    }

    @Override
    public List<Categoria> getAllCategorias() throws SQLException {
        List<Categoria> list = new ArrayList<>();
        String sql = "SELECT * FROM Categoria";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Categoria c = new Categoria();
                c.setId(rs.getInt("id"));
                c.setNome(rs.getString("nome"));
                list.add(c);
            }
        }
        System.out.println("Lista de categorias: " + list);
        return list;

    }
}
