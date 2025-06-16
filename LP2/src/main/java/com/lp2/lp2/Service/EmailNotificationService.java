package com.lp2.lp2.Service;

import com.lp2.lp2.Util.GmailSender;

public class EmailNotificationService {

    public void enviarSemCreditos(String recipientEmail, String recipientName) {
        String subject = "Atenção: Créditos Esgotados";
        String body = "Olá " + recipientName + ",\n\n"
                + "Os seus créditos chegaram ao fim. Para continuar utilizando os serviços, por favor, carregue a sua conta.\n\n"
                + "Atenciosamente,\nLeilões Express";
        GmailSender.sendEmail(recipientEmail, subject, body);
    }

    public void enviarSemLogin3Meses(String recipientEmail, String recipientName) {
        String subject = "Sentimos a sua falta!";
        String body = "Olá " + recipientName + ",\n\n"
                + "Verificámos que há mais de 3 meses não faz login à nossa plataforma. Volte a participar dos leilões com oportunidades imperdíveis!\n\n"
                + "Atenciosamente,\nLeilões Express";
        GmailSender.sendEmail(recipientEmail, subject, body);
    }

    public void enviarEmailVencedorLeilao(String destinatarioEmail, String nomeDestinatario, int leilaoId, String leilaoNome) {
        String subject = "Parabéns! Você ganhou o leilão";
        String body = "PARABÉNS!!!! " + nomeDestinatario + ",\n\n" +
                "Informamos que hoje é um dia de sorte! Você é o vencedor do leilão '" + leilaoNome + "' com o id " + leilaoId + "!\n\n" +
                "Para mais informações, contacte lp2sendmail@gmail.com\n\n" +
                "Atenciosamente,\nLeilões Express";

        String htmlBody = "<div style='font-family: Arial, sans-serif; color: #333;'>"
                + "<h3>PARABÉNS!!!! " + nomeDestinatario + ",</h3>"
                + "<p>Informamos que hoje é um dia de sorte! Você é o vencedor do leilão <strong>" + leilaoNome + "</strong> com o id <strong>" + leilaoId + "</strong>!</p>"
                + "<p>Para mais informações, contacte <a href='mailto:lp2sendmail@gmail.com'>lp2sendmail@gmail.com</a>.</p>"
                + "<br>"
                + "<p>Atenciosamente,</p>"
                + "<p><strong>Leilões Express</strong></p>"
                + "</div>";

        GmailSender.sendEmail(destinatarioEmail, subject, htmlBody);
    }
    public void enviarEmailNovaProposta(String email, String nome, int leilaoId) {
        String subject = "Nova proposta de negociação";
        String body = "Olá " + nome + ",\n\nRecebemos uma nova proposta para o leilão " + leilaoId + ".";
        GmailSender.sendEmail(email, subject, body);
    }

    public void enviarEmailRespostaProposta(String email, String estado, int leilaoId) {
        String subject = "Resposta à sua proposta";
        String body = "A sua proposta no leilão " + leilaoId + " foi " + estado + ".";
        GmailSender.sendEmail(email, subject, body);
    }

    public void enviarEmailAprovacaoPontos(String destinatarioEmail, String nomeCliente, int pontosAprovados) {
        String subject = "Seus créditos foram aprovados!";
        String htmlBody = "<div style='font-family:Arial,sans-serif;color:#333'>"
                + "<h3>Olá, " + nomeCliente + "!</h3>"
                + "<p>Informamos que o seu pedido de <b>" + pontosAprovados + " créditos</b> foi aprovado com sucesso.</p>"
                + "<p>Já pode utilizá-los para participar nos nossos leilões.</p>"
                + "<br/><p>Qualquer dúvida, contacte <a href='mailto:lp2sendmail@gmail.com'>lp2sendmail@gmail.com</a></p>"
                + "<p><b>Leilões Express</b></p>"
                + "</div>";
        com.lp2.lp2.Util.GmailSender.sendEmail(destinatarioEmail, subject, htmlBody);
    }



    // Exemplo para o relatório DIÁRIO com anexo
   /* public void enviarRelatorioGestor(String gestorEmail, String csvFilePath) {
        String subject = "Relatório Diário - Leilões Express";
        String body = "Segue em anexo o relatório diário dos leilões e clientes.";
        // Aqui precisa adaptar o GmailSender para permitir envio com anexo (faça essa extensão apenas aqui)
        GmailSender.sendEmailWithAttachment(gestorEmail, subject, body, csvFilePath);
    }*/

    // Outros métodos conforme necessidade...
}