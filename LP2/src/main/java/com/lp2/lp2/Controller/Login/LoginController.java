package com.lp2.lp2.Controller.Login;

import com.lp2.lp2.DAO.IDAO.IUserDAO;
import com.lp2.lp2.DAO.UserDAO;
import com.lp2.lp2.Model.User;
import com.lp2.lp2.Session.Session;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Optional;

/**
 * Controlador para a tela de login. Gerencia a autenticação do usuário e o carregamento do menu correspondente
 * ao papel do usuário (atleta ou gerente).
 */
public class LoginController {

    @FXML
    private TextField UserNameTextField;  // Campo para o nome de usuário

    @FXML
    private PasswordField passwordTextField;  // Campo para a senha

    @FXML
    private Button loginButton;  // Botão para realizar o login
    @FXML
    private Button registarButton;

    // Instância da DAO via interface para buscar usuários
    private final IUserDAO userDAO = new UserDAO();

    // Armazenamento global do usuário logado
    public static User loggedUser;

    /**
     * Método chamado quando o botão de login é clicado.
     * Realiza a autenticação do usuário com base no nome de usuário e senha fornecidos.
     */
    @FXML
    private void onLoginButtonClicked() {
        String username = UserNameTextField.getText(); // Obtém o nome de usuário
        String password = passwordTextField.getText(); // Obtém a senha

        // Verifica se a autenticação foi bem-sucedida
        if (authenticate(username, password)) {
            // Busca o usuário no banco de dados
            Optional<User> loggedUserOptional = userDAO.findByUsername(username);

            // Verifica se o usuário foi encontrado
            if (loggedUserOptional.isPresent()) {
                User loggedUser = loggedUserOptional.get(); // Extrai o valor do Optional

                // Salva o usuário na sessão
                Session.setUser(loggedUser);

                // Exibe alerta de sucesso
                showAlert(Alert.AlertType.INFORMATION, "Login bem-sucedido!", "Bem-vindo, " + username + "!");

                // Carrega o menu correspondente ao papel do usuário
                loadMenuForRole(loggedUser.getRole());
            } else {
                showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao carregar os dados do usuário.");
            }
        }
    }
    @FXML
    void onRegistarButtonClicked(MouseEvent event) {


    }
    /**
     * Método para autenticar o usuário com base no nome de usuário e senha fornecidos.
     *
     * @param username O nome de usuário fornecido.
     * @param password A senha fornecida.
     * @return Retorna true se a autenticação for bem-sucedida, caso contrário, retorna false.
     */
    private boolean authenticate(String username, String password) {
        Optional<User> userOptional = userDAO.findByUsername(username);

        // Verifica se o usuário existe
        if (!userOptional.isPresent()) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Usuário não encontrado.");
            return false;
        }

        User user = userOptional.get();

        // Verifica se o usuário está inativo
        if (user.isInative()) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Usuário foi excluído.");
            return false;
        }

        // Gera o hash da senha fornecida
        String passwordHash = hashPassword(password);

        // Verifica se a senha está correta
        if (!passwordHash.equals(user.getPasswordHash())) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Senha incorreta.");
            return false;
        }

        // Verifica se o papel do usuário é válido
        if (!user.getRole().equals("cliente") && !user.getRole().equals("manager")) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Papel inválido para o usuário.");
            return false;
        }

        return true;
    }

    /**
     * Carrega o menu adequado com base no papel do usuário (atleta ou gerente).
     *
     * @param role O papel do usuário (atleta ou gerente).
     */
    private void loadMenuForRole(String role) {
        try {
            Stage primaryStage = (Stage) UserNameTextField.getScene().getWindow();
            FXMLLoader loader;

            // Carrega a tela correspondente ao papel do usuário
            if ("cliente".equalsIgnoreCase(role)) {
                loader = new FXMLLoader(getClass().getResource("/com/lp3_grupo5/lp2/Menus/Menu_Inicial.fxml"));
                System.out.println(role);
            } else if ("manager".equalsIgnoreCase(role)) {
                loader = new FXMLLoader(getClass().getResource("/com/lp3_grupo5/lp2/Menus/Menu_Inicial.fxml"));
                System.out.println(role);
            } else {
                System.out.println(role);
                throw new IllegalArgumentException("Papel inválido: " + role);

            }

            // Carrega a cena e exibe no estágio
            Pane menu = loader.load();
            Scene scene = new Scene(menu);
            primaryStage.setScene(scene);
            primaryStage.setTitle(role.equals("cliente") ? "Menu do Atleta" : "Menu do Gerente");
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erro", "Não foi possível carregar o menu.");
        }
    }

    /**
     * Método para tratar o evento de pressionamento de tecla. Caso a tecla pressionada seja "Enter",
     * o login será realizado.
     *
     * @param keyEvent Evento de pressionamento de tecla.
     */
    @FXML
    public void onKeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ENTER)) {
            onLoginButtonClicked();
        }
    }

    /**
     * Método para gerar o hash de uma senha usando SHA-256.
     *
     * @param password A senha em texto simples.
     * @return O hash da senha em Base64.
     */
    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Erro ao encriptar a senha: " + e.getMessage(), e);
        }
    }

    /**
     * Exibe um alerta com o tipo, título e mensagem fornecidos.
     *
     * @param alertType O tipo do alerta (informação, erro, etc.).
     * @param title     O título do alerta.
     * @param message   A mensagem a ser exibida no alerta.
     */
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(alertType);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }
}

