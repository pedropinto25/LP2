package com.lp2.lp2.Model;

import java.math.BigDecimal;

public class Agente {
    private int id;
    private int clienteId;
    private int leilaoId;
    private int ordem;
    private BigDecimal incremento;
    private BigDecimal limite;
    private boolean ativo;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getClienteId() { return clienteId; }
    public void setClienteId(int clienteId) { this.clienteId = clienteId; }

    public int getLeilaoId() { return leilaoId; }
    public void setLeilaoId(int leilaoId) { this.leilaoId = leilaoId; }

    public int getOrdem() { return ordem; }
    public void setOrdem(int ordem) { this.ordem = ordem; }

    public BigDecimal getIncremento() { return incremento; }
    public void setIncremento(BigDecimal incremento) { this.incremento = incremento; }

    public BigDecimal getLimite() { return limite; }
    public void setLimite(BigDecimal limite) { this.limite = limite; }

    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }
}