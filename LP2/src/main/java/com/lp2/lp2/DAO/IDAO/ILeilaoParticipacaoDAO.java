package com.lp2.lp2.DAO.IDAO;

import com.lp2.lp2.Model.LeilaoParticipacao;

import java.sql.SQLException;
import java.util.List;

public interface ILeilaoParticipacaoDAO {
    void addParticipacao(LeilaoParticipacao participacao) throws SQLException;
    void updateParticipacao(LeilaoParticipacao participacao) throws SQLException;
    void deleteParticipacao(int id) throws SQLException;
    LeilaoParticipacao getParticipacaoById(int id) throws SQLException;
    List<LeilaoParticipacao> getParticipacoesByLeilaoId(int leilaoId) throws SQLException;
    List<LeilaoParticipacao> getAllParticipacoes() throws SQLException;
}