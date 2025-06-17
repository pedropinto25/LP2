package com.lp2.lp2.DAO.IDAO;

import com.lp2.lp2.Model.LeilaoClassificacao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface ILeilaoClassificacaoDAO {
    void addClassificacao(LeilaoClassificacao classificacao) throws SQLException;
    List<LeilaoClassificacao> getClassificacoesByLeilaoId(int leilaoId) throws SQLException;
    boolean existsByClienteLeilao(int clienteId, int leilaoId) throws SQLException;
    Double getAverageRatingForLeilao(int leilaoId) throws SQLException;
    Double getAverageRatingOverall() throws SQLException;
    Map<Integer, Double> getAverageRatingPerLeilao() throws SQLException;

}