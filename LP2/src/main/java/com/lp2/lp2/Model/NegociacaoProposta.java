package com.lp2.lp2.Model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class NegociacaoProposta {
    private int id;
    private int leilaoId;
    private int clienteId;
    private BigDecimal valor;
    private String estado;
    private Timestamp data;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getLeilaoId() { return leilaoId; }
    public void setLeilaoId(int leilaoId) { this.leilaoId = leilaoId; }

    public int getClienteId() { return clienteId; }
    public void setClienteId(int clienteId) { this.clienteId = clienteId; }

    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public Timestamp getData() { return data; }
    public void setData(Timestamp data) { this.data = data; }
}