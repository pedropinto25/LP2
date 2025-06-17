package com.lp2.lp2.DAO.IDAO;

import com.lp2.lp2.Model.Agente;
import java.sql.SQLException;
import java.util.List;

public interface IAgenteDAO {
    void addAgente(Agente agente) throws SQLException;
    void updateAgente(Agente agente) throws SQLException;
    void deleteAgente(int id) throws SQLException;
    Agente getAgenteById(int id) throws SQLException;
    List<Agente> getAgentesByLeilao(int leilaoId) throws SQLException;
    List<Agente> getAllAgentes() throws SQLException;
}