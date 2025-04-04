package com.lp2.lp2.DAO.IDAO;

import com.lp2.lp2.Model.User;

import java.util.List;
import java.util.Optional;

public interface IUserDAO {
    /**
     * Busca um utilizador pelo nome de utilizador.
     *
     * @param username O nome de utilizador a ser buscado.
     * @return Um Optional contendo o utilizador, caso exista.
     */
    Optional<User> findByUsername(String username);

    /**
     * Busca um utilizador pelo seu ID.
     *
     * @param userId O ID do utilizador a ser buscado.
     * @return Um Optional contendo o utilizador, caso exista.
     */
    Optional<User> findById(int userId);

    /**
     * Atualiza a palavra-passe de um utilizador na base de dados.
     *
     * @param userId      O ID do utilizador.
     * @param newPassword A nova palavra-passe em texto simples (será armazenada como texto simples na base de dados).
     * @return true se a palavra-passe foi atualizada com sucesso, false caso contrário.
     */
    boolean updatePassword(int userId, String newPassword);

    /**
     * Verifica se a palavra-passe fornecida corresponde à palavra-passe armazenada na base de dados.
     *
     * @param plainPassword    A palavra-passe fornecida pelo utilizador (em texto simples).
     * @param databasePassword A palavra-passe armazenada na base de dados (em texto simples).
     * @return true se as palavras-passe correspondem, false caso contrário.
     */
    boolean verifyPassword(String plainPassword, String databasePassword);

    /**
     * Atualiza as informações de um utilizador na base de dados.
     *
     * @param user O objeto utilizador com os dados a serem atualizados.
     * @return true se a atualização foi bem-sucedida, false caso contrário.
     */
    void update(User user);

    List<User> getAllUsers();

    boolean updatePasswordAndIncripted(int userId, String newPasswordHash, int incripted);

    List<User> getAllUsers1();
}


