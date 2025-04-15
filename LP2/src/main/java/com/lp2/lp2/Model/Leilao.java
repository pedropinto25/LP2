package com.lp2.lp2.Model;

import java.math.BigDecimal;
import java.util.Date;

public class Leilao {
    private int id;
    private String nome;
    private String descricao;
    private String tipo;
    private Date dataInicio;
    private Date dataFim;
    private BigDecimal valorMinimo;
    private BigDecimal valorMaximo;
    private BigDecimal multiploLance;
    private boolean inativo;
    private boolean vendido;

    public Leilao(String nome, String descricao, String tipo, Date dataInicio, Date dataFim, BigDecimal valorMinimo, BigDecimal valorMaximo, BigDecimal multiploLance, boolean inativo, boolean vendido) {
        this.nome = nome;
        this.descricao = descricao;
        this.tipo = tipo;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.valorMinimo = valorMinimo;
        this.valorMaximo = valorMaximo;
        this.multiploLance = multiploLance;
        this.inativo = inativo;
        this.vendido = vendido;
    }

    public Leilao() {

    }

    @Override
    public String toString(){
        return  id + " - " + nome;
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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public java.sql.Date getDataInicio() {
        return (java.sql.Date) dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public java.sql.Date getDataFim() {
        return (java.sql.Date) dataFim;
    }

    public void setDataFim(Date dataFim) {
        this.dataFim = dataFim;
    }

    public BigDecimal getValorMinimo() {
        return valorMinimo;
    }

    public void setValorMinimo(BigDecimal valorMinimo) {
        this.valorMinimo = valorMinimo;
    }

    public BigDecimal getValorMaximo() {
        return valorMaximo;
    }

    public void setValorMaximo(BigDecimal valorMaximo) {
        this.valorMaximo = valorMaximo;
    }

    public BigDecimal getMultiploLance() {
        return multiploLance;
    }

    public void setMultiploLance(BigDecimal multiploLance) {
        this.multiploLance = multiploLance;
    }

    public boolean getInativo() {
        return inativo;
    }

    public void setInativo(boolean inativo) {
        this.inativo = inativo;
    }

    public boolean getVendido() {
        return vendido;
    }

    public void setVendido(boolean vendido) {
        this.vendido = vendido;
    }
}
