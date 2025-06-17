package com.lp2.lp2.Model;

import java.sql.Date;

public class LeilaoClassificacao {
    private int id;
    private int classificacao;
    private String comentario;
    private Date dataClassificacao;
    private int clienteId;
    private int leilaoId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClassificacao() {
        return classificacao;
    }

    public void setClassificacao(int classificacao) {
        this.classificacao = classificacao;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Date getDataClassificacao() {
        return dataClassificacao;
    }

    public void setDataClassificacao(Date dataClassificacao) {
        this.dataClassificacao = dataClassificacao;
    }

    public int getClienteId() {
        return clienteId;
    }

    public void setClienteId(int clienteId) {
        this.clienteId = clienteId;
    }

    public int getLeilaoId() {
        return leilaoId;
    }

    public void setLeilaoId(int leilaoId) {
        this.leilaoId = leilaoId;
    }
}