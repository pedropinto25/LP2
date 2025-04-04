package com.lp2.lp2.Controller.Login;
import com.lp2.lp2.DAO.IDAO.IUserDAO;
import com.lp2.lp2.DAO.UserDAO;
import com.lp2.lp2.Model.User;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;
public class UserEncryption {
    private final IUserDAO userDAO = new UserDAO();

    public void encryptPasswords() {
        // Buscar todos os utilizadores
        List<User> users = userDAO.getAllUsers1();

        for (User user : users) {
            // Verificar se a coluna incripted é 0
            if (!user.isIncripted()) {
                // Encriptar a palavra-passe
                String encryptedPassword = hashPassword(user.getPasswordHash());

                // Atualizar a palavra-passe encriptada e definir incripted como 1
                userDAO.updatePasswordAndIncripted(user.getId(), encryptedPassword, 1);
            }
        }
    }

    public void encryptPasswordsCliente() {
        // Buscar todos os utilizadores
        List<User> users = userDAO.getAllUClientes();

        for (User user : users) {
            // Verificar se a coluna incripted é 0
            if (!user.isIncripted()) {
                // Encriptar a palavra-passe
                String encryptedPassword = hashPassword(user.getPasswordHash());

                // Atualizar a palavra-passe encriptada e definir incripted como 1
                userDAO.updatePasswordAndIncriptedCliente(user.getId(), encryptedPassword, 1);
            }
        }
    }

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Erro ao encriptar a senha: " + e.getMessage(), e);
        }
    }
}
