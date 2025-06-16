package com.lp2.lp2.DAO.IDAO;

import com.lp2.lp2.Model.Leilao;

import java.sql.SQLException;
import java.util.List;

public interface ILeilaoDAO {
    void addLeilao(Leilao leilao) throws SQLException;
    void updateLeilao(Leilao leilao) throws SQLException;
    void deleteLeilao(int id) throws SQLException;
    Leilao getLeilaoById(int id) throws SQLException;
    List<Leilao> getAllLeiloes() throws SQLException;

    void inserirLeiloes(List<Leilao> leiloes) throws SQLException;
}
