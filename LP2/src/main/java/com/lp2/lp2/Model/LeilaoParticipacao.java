package com.lp2.lp2.Model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class LeilaoParticipacao {
    private int id;
    private int leilaoId;
    private int clienteId;
    private Timestamp dataParticipacao;
    private BigDecimal valorLance;

    public LeilaoParticipacao(int leilaoId, int clienteId, int totalLances, BigDecimal maiorLance) {
    }
    public LeilaoParticipacao(){

    }
    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLeilaoId() {
        return leilaoId;
    }

    public void setLeilaoId(int leilaoId) {
        this.leilaoId = leilaoId;
    }

    public int getClienteId() {
        return clienteId;
    }

    public void setClienteId(int clienteId) {
        this.clienteId = clienteId;
    }

    public Timestamp getDataParticipacao() {
        return dataParticipacao;
    }

    public void setDataParticipacao(Timestamp dataParticipacao) {
        this.dataParticipacao = dataParticipacao;
    }

    public BigDecimal getValorLance() {
        return valorLance;
    }

    public void setValorLance(BigDecimal valorLance) {
        this.valorLance = valorLance;
    }
}