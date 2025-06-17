package com.lp2.lp2.Service;

import com.lp2.lp2.DAO.UserDAO;
import com.lp2.lp2.Model.User;

import java.time.LocalDateTime;
import java.util.List;

public class Inatividade3Meses {

    private final UserDAO userDAO = new UserDAO();
    private final EmailNotificationService emailService = new EmailNotificationService();

    public void inatividade3meses() {
        List<User> todosUsuarios = userDAO.getAllUsers();
        LocalDateTime agora = LocalDateTime.now();
        LocalDateTime limite = agora.minusMonths(3);

        for (User user : todosUsuarios) {
            if (!"cliente".equalsIgnoreCase(user.getRole())) continue;
            if (user.getUltimoLogin() == null) continue;
            if (!Boolean.TRUE.equals(user.getApproved())) continue;

            LocalDateTime ultimoLogin = user.getUltimoLogin().toLocalDateTime();

            if (ultimoLogin.isBefore(limite)) {
                System.out.println("Enviando e-mail para: " + user.getUsername());
                emailService.enviarSemLogin3Meses(user.getUsername(), user.getUsername());
            }
        }
    }
}
