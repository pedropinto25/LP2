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
        String sql = "INSERT INTO Pontos (cliente_id, pontos) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, pontos.getClienteId());
            stmt.setInt(2, pontos.getPontos());
            stmt.executeUpdate();
        }
    }

    @Override
    public void updatePontos(Pontos pontos) throws SQLException {
        String sql = "UPDATE Pontos SET pontos = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, pontos.getPontos());
            stmt.setInt(2, pontos.getId());
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
                pontosList.add(pontos);
            }
        }
        return pontosList;
    }

    @Override
    public int verificarPontos(int clienteId) throws SQLException {
        String sql = "SELECT COALESCE(SUM(pontos), 0) AS total_pontos FROM Pontos WHERE cliente_id = ? AND approved = 1";
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
        String sqlSelect = "SELECT id, pontos FROM Pontos WHERE cliente_id = ? AND approved = 1 AND pontos > 0 ORDER BY id";
        List<Integer> ids = new ArrayList<>();
        List<Integer> valores = new ArrayList<>();
        int restante = pontos;
        try (PreparedStatement stmt = connection.prepareStatement(sqlSelect)) {
            stmt.setInt(1, clienteId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next() && restante > 0) {
                    int id = rs.getInt("id");
                    int valor = rs.getInt("pontos");
                    ids.add(id);
                    valores.add(valor);
                    restante -= valor;
                }
            }
        }
        restante = pontos;
        for (int i = 0; i < ids.size() && restante > 0; i++) {
            int id = ids.get(i);
            int valor = valores.get(i);
            int subtrair = Math.min(valor, restante);
            String sqlUpdate = "UPDATE Pontos SET pontos = pontos - ? WHERE id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sqlUpdate)) {
                stmt.setInt(1, subtrair);
                stmt.setInt(2, id);
                stmt.executeUpdate();
            }
            restante -= subtrair;
        }
        // Pode lançar exception se não havia pontos suficientes
    }


    public List<Pontos> getAllPontosToAprove() throws SQLException {
        List<Pontos> lista = new ArrayList<>();
        String sql = "SELECT * FROM Pontos WHERE approved = 0";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Pontos ponto = new Pontos();
                ponto.setId(rs.getInt("id"));
                ponto.setClienteId(rs.getInt("clienteId"));
                ponto.setPontos(rs.getInt("pontos"));
                ponto.setApproved(rs.getBoolean("approved"));
                lista.add(ponto);
            }
        }
        return lista;
    }

    public void aprovarPontos(int id) throws SQLException {
        String sql = "UPDATE Pontos SET approved = 1 WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public void pedirPontos(int clienteId, int pontos) throws SQLException {
        String sql = "INSERT INTO Pontos (cliente_id, pontos, approved) VALUES (?, ?, 0)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, clienteId);
            stmt.setInt(2, pontos);
            stmt.executeUpdate();
        }
    }

    public List<Integer> getClientesComPedidosPontos() throws SQLException {
        String sql = "SELECT DISTINCT cliente_id FROM Pontos WHERE approved = 1";
        List<Integer> clientes = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    clientes.add(rs.getInt("cliente_id"));
                }
            }
        }
        return clientes;
    }



}