package com.lp2.lp2.Service;

import com.lp2.lp2.DAO.PontosDAO;
import com.lp2.lp2.DAO.UserDAO;
import com.lp2.lp2.Model.User;

import java.sql.SQLException;
import java.util.List;

public class SemCreditos {

    private final UserDAO userDAO = new UserDAO();
    private final PontosDAO pontosDAO;
    private final EmailNotificationService emailService = new EmailNotificationService();

    public SemCreditos() throws SQLException {
        this.pontosDAO = new PontosDAO();
    }

    public void clientesSemCreditos() {
        try {
            // Busca todos os usuários aprovados do tipo cliente
            List<User> todosUsuarios = userDAO.getAllUsers();
            for (User user : todosUsuarios) {
                if (!"cliente".equalsIgnoreCase(user.getRole())) continue;
                if (!Boolean.TRUE.equals(user.getApproved())) continue;

                int pontos = pontosDAO.verificarPontos(user.getId());
                if (pontos == 0) {
                    // Enviar e-mail de alerta sem créditos
                    emailService.enviarSemCreditos(user.getUsername(), user.getUsername());
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace(); // ou log adequado
        }
    }
}
