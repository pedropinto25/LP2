package com.lp2.lp2.DAO.IDAO;

import com.lp2.lp2.Model.Pontos;

import java.sql.SQLException;
import java.util.List;

public interface IPontosDAO {
    void addPontos(Pontos pontos) throws SQLException;
    void updatePontos(Pontos pontos) throws SQLException;
    void deletePontos(int id) throws SQLException;
    Pontos getPontosById(int id) throws SQLException;
    List<Pontos> getAllPontos() throws SQLException;
    int verificarPontos(int clienteId) throws SQLException;
    void adicionarPontos(int clienteId, int pontos) throws SQLException;
    void removerPontos(int clienteId, int pontos) throws SQLException;
}
