package com.lp2.lp2.Model;

import java.util.Date;

public class Cliente {
    private int id;
    private String nome;
    private String morada;
    private Date dataNascimento;
    private String email;
    private String senha;
    private Boolean approved;

    public Cliente( String nome, String morada, Date dataNascimento, String email, String senha) {
        this.nome = nome;
        this.morada = morada;
        this.dataNascimento = dataNascimento;
        this.email = email;
        this.senha = senha;
    }

    public Cliente() {

    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getMorada() {
        return morada;
    }

    public void setMorada(String morada) {
        this.morada = morada;
    }

    public java.sql.Date getDataNascimento() {
        return (java.sql.Date) dataNascimento;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getApproved() {
        return approved;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }
}
