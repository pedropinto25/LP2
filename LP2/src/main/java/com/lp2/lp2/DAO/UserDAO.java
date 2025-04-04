package com.lp2.lp2.DAO;


import com.lp2.lp2.DAO.IDAO.IUserDAO;
import com.lp2.lp2.Infrastucture.Connection.DBConnection;
import com.lp2.lp2.Model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementação da interface {@link IUserDAO} para gerenciar operações de CRUD na tabela "Users" do banco de dados.
 * Este DAO permite a busca de usuários por nome de usuário ou ID, a atualização de senhas e dados de usuários.
 */
public class UserDAO implements IUserDAO {

    /**
     * Recupera um usuário do banco de dados com base no nome de usuário.
     *
     * @param username O nome de usuário a ser pesquisado.
     * @return Um {@link Optional<User>} que contém o usuário encontrado, ou vazio caso não exista.
     */
    @Override
    public Optional<User> findByUsername(String username) {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM Users WHERE email = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("email"));
                user.setPasswordHash(rs.getString("password_hash")); // A senha em texto plano do banco
                user.setRole(rs.getString("role"));
                //user.setInative(rs.getBoolean("inative"));
                return Optional.of(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    /**
     * Recupera um utilizador do banco de dados com base no ID.
     *
     * @param userId O ID do utilizador a ser pesquisado.
     * @return Um {@link Optional<User>} que contém o utilizador encontrado, ou vazio caso não exista.
     */
    @Override
    public Optional<User> findById(int userId) {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM Users WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("email"));
                user.setPasswordHash(rs.getString("password_hash")); // A senha em texto plano do banco
                user.setRole(rs.getString("role"));
                //user.setInative(rs.getBoolean("inative"));
                return Optional.of(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    /**
     * Atualiza a senha de um utilizador no banco de dados.
     *
     * @param userId O ID do utilizador que terá sua senha atualizada.
     * @param newPassword A nova senha a ser definida (em texto simples).
     * @return {@code true} se a atualização for bem-sucedida, {@code false} caso contrário.
     */
    @Override
    public boolean updatePassword(int userId, String newPassword) {
        try (Connection conn = DBConnection.getConnection()) {
            // Atualiza a senha (em texto simples, sem criptografia)
            String sql = "UPDATE Users SET password_hash = ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, newPassword);  // Senha em texto normal, não criptografada
            stmt.setInt(2, userId);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Atualiza os dados de um utilizador no banco de dados.
     *
     * @param user O objeto {@link User} contendo os dados a serem atualizados.
     */
    @Override
    public void update(User user) {
        String sql = "UPDATE users SET email = ?, password_hash = ?, role = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPasswordHash()); // Armazena o novo hash da senha
            stmt.setString(3, user.getRole());
            //stmt.setBoolean(4, user.isInative());
            //stmt.setObject(5, user.getAthleteId(), Types.INTEGER);
            stmt.setInt(6, user.getId());

            stmt.executeUpdate(); // Executa a atualização no banco
        } catch (SQLException e) {
            e.printStackTrace(); // Trate o erro conforme necessário
        }
    }

    /**
     * Verifica se a senha fornecida (em texto simples) corresponde à senha armazenada no banco de dados.
     *
     * @param plainPassword A senha fornecida pelo utilizador (em texto simples).
     * @param databasePassword A senha armazenada no banco de dados (em texto simples).
     * @return {@code true} se as senhas forem iguais, {@code false} caso contrário.
     */
    public boolean verifyPassword(String plainPassword, String databasePassword) {
        // Compara diretamente a senha fornecida com a armazenada (sem criptografia)
        return plainPassword.equals(databasePassword);
    }
    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM Users";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            // Percorrer todos os resultados da consulta
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("email"));
                user.setPasswordHash(rs.getString("password_hash")); // A senha em texto plano do banco
                user.setRole(rs.getString("role"));
                //user.setInative(rs.getBoolean("inative"));
                users.add(user); // Adiciona o utilizador à lista
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Se necessário, você pode lançar uma exceção personalizada aqui
        }
        return users; // Retorna a lista de utilizadors
    }

    /**
     * Retorna o nome do atleta associado a um utilizador.
     *
     * @param userId O ID do utilizador (user_id).
     * @return O nome do atleta, ou null se não for encontrado.
     */
    public String findAthleteNameByUserId(int userId) {
        String query = """
        SELECT a.name
        FROM Users u
        INNER JOIN Athletes a ON u.athlete_id = a.id
        WHERE u.id = ?;
    """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("name"); // Retorna o nome do atleta
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null; // Retorna null caso não encontre
    }
    @Override
    public boolean updatePasswordAndIncripted(int userId, String newPasswordHash, int incripted) {
        String sql = "UPDATE Users SET password_hash = ?, encrypted = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newPasswordHash);
            pstmt.setInt(2, incripted);
            pstmt.setInt(3, userId);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public List<User> getAllUsers1() {
        List<User> users = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM Users";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            // Percorrer todos os resultados da consulta
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("email"));
                user.setPasswordHash(rs.getString("password_hash")); // A senha em texto plano do banco
                user.setRole(rs.getString("role"));
                //user.setInative(rs.getBoolean("inative"));
                user.setIncripted(rs.getBoolean("encrypted")); // Adiciona o campo incripted
                users.add(user); // Adiciona o utilizador à lista
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Se necessário, você pode lançar uma exceção personalizada aqui
        }
        return users; // Retorna a lista de utilizadors
    }

}
