package com.lp2.lp2.Model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Lance {
    private int id;
    private BigDecimal valor;
    private Timestamp dataHora;
    private int clienteId;
    private int leilaoId;

    public Lance(int id, BigDecimal valor, int clienteId, Timestamp dataHora, int leilaoId) {
        this.id = id;
        this.valor = valor;
        this.clienteId = clienteId;
        this.dataHora = dataHora;
        this.leilaoId = leilaoId;
    }

    public Lance() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public int getClienteId() {
        return clienteId;
    }

    public void setClienteId(int clienteId) {
        this.clienteId = clienteId;
    }

    public Timestamp getDataHora() {
        return dataHora;
    }

    public void setDataHora(Timestamp dataHora) {
        this.dataHora = dataHora;
    }

    public int getLeilaoId() {
        return leilaoId;
    }

    public void setLeilaoId(int leilaoId) {
        this.leilaoId = leilaoId;
    }

}
