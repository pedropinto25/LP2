package com.lp2.lp2.Model;

public class Pontos {
    private int id;
    private int clienteId;
    private int pontos;
    private int leilaoId;

    // Getters e setters
    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public int getClienteId() { return clienteId; }

    public void setClienteId(int clienteId) {this.clienteId = clienteId;}

    public int getPontos() {return pontos;}

    public void setPontos(int pontos) {this.pontos = pontos;}

    public int getLeilaoId() {return leilaoId;}

    public void setLeilaoId(int leilaoId) {this.leilaoId = leilaoId;}
}
