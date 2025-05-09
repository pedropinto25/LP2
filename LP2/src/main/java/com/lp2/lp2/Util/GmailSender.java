package com.lp2.lp2.Util;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.*;

public class GmailSender {

    // Função para enviar e-mail
    public static void sendEmail(String to, String subject, String body) {
        String from = "lp3sendmail@gmail.com"; // Seu e-mail do Gmail
        String host = "smtp.gmail.com";         // Servidor SMTP do Gmail

        // Propriedades para configurar a conexão com o Gmail
        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", host);
        properties.setProperty("mail.smtp.port", "587");
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.starttls.enable", "true"); // Necessário para segurança

        // Autenticação no servidor SMTP
        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("lp3sendmail@gmail.com", "ewlh wtbc zugp btyw"); // Senha de app
            }
        });

        try {
            // Criação da mensagem
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(subject);

            // Detecta se é HTML e usa o tipo correto
            if (body.contains("<") && body.contains(">")) {
                message.setContent(body, "text/html; charset=utf-8");
            } else {
                message.setText(body); // Texto puro
            }

            // Enviar e-mail
            Transport.send(message);
            System.out.println("E-mail enviado com sucesso!");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // Exemplo de uso com HTML
        String htmlContent = "<h2>Olá!</h2><p>Este é um teste de <strong>e-mail com HTML</strong>.</p>";
        sendEmail("destinatario@example.com", "Teste com HTML", htmlContent);
    }
}
