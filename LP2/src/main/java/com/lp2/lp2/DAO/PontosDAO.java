package com.lp2.lp2.DAO;

import com.lp2.lp2.DAO.IDAO.IPontosDAO;
import com.lp2.lp2.Infrastucture.Connection.DBConnection;
import com.lp2.lp2.Model.Pontos;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PontosDAO implements IPontosDAO {
    private Connection connection;

    public PontosDAO() throws SQLException {
        connection = DBConnection.getConnection(); // inicializar a conexão com o banco de dados
    }

    @Override
    public void addPontos(Pontos pontos) throws SQLException {
        String sql = "INSERT INTO Pontos (cliente_id, pontos, leilao_id) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, pontos.getClienteId());
            stmt.setInt(2, pontos.getPontos());
            stmt.setInt(3, pontos.getLeilaoId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void updatePontos(Pontos pontos) throws SQLException {
        String sql = "UPDATE Pontos SET pontos = ?, leilao_id = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, pontos.getPontos());
            stmt.setInt(2, pontos.getLeilaoId());
            stmt.setInt(3, pontos.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void deletePontos(int id) throws SQLException {
        String sql = "DELETE FROM Pontos WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public Pontos getPontosById(int id) throws SQLException {
        String sql = "SELECT * FROM Pontos WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Pontos pontos = new Pontos();
                    pontos.setId(rs.getInt("id"));
                    pontos.setClienteId(rs.getInt("cliente_id"));
                    pontos.setPontos(rs.getInt("pontos"));
                    pontos.setLeilaoId(rs.getInt("leilao_id"));
                    return pontos;
                }
            }
        }
        return null;
    }

    @Override
    public List<Pontos> getAllPontos() throws SQLException {
        String sql = "SELECT * FROM Pontos";
        List<Pontos> pontosList = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Pontos pontos = new Pontos();
                pontos.setId(rs.getInt("id"));
                pontos.setClienteId(rs.getInt("cliente_id"));
                pontos.setPontos(rs.getInt("pontos"));
                pontos.setLeilaoId(rs.getInt("leilao_id"));
                pontosList.add(pontos);
            }
        }
        return pontosList;
    }

    @Override
    public int verificarPontos(int clienteId) throws SQLException {
        String sql = "SELECT SUM(pontos) AS total_pontos FROM Pontos WHERE cliente_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, clienteId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total_pontos");
                }
            }
        }
        return 0;
    }

    @Override
    public void adicionarPontos(int clienteId, int pontos) throws SQLException {
        String sqlVerificar = "SELECT id, pontos FROM Pontos WHERE cliente_id = ?";
        try (PreparedStatement stmtVerificar = connection.prepareStatement(sqlVerificar)) {
            stmtVerificar.setInt(1, clienteId);
            try (ResultSet rs = stmtVerificar.executeQuery()) {
                if (rs.next()) {
                    // Se já existe um registro, atualize os pontos
                    int id = rs.getInt("id");
                    int pontosExistentes = rs.getInt("pontos");
                    String sqlAtualizar = "UPDATE Pontos SET pontos = ? WHERE id = ?";
                    try (PreparedStatement stmtAtualizar = connection.prepareStatement(sqlAtualizar)) {
                        stmtAtualizar.setInt(1, pontosExistentes + pontos);
                        stmtAtualizar.setInt(2, id);
                        stmtAtualizar.executeUpdate();
                    }
                } else {
                    // Se não existe um registro, insira um novo
                    String sqlInserir = "INSERT INTO Pontos (cliente_id, pontos) VALUES (?, ?)";
                    try (PreparedStatement stmtInserir = connection.prepareStatement(sqlInserir)) {
                        stmtInserir.setInt(1, clienteId);
                        stmtInserir.setInt(2, pontos);
                        stmtInserir.executeUpdate();
                    }
                }
            }
        }
    }


    @Override
    public void removerPontos(int clienteId, int pontos) throws SQLException {
        String sql = "UPDATE Pontos SET pontos = pontos - ? WHERE cliente_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, pontos);
            stmt.setInt(2, clienteId);
            stmt.executeUpdate();
        }
    }

}
