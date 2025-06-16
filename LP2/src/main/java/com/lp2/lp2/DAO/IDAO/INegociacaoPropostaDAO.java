package com.lp2.lp2.DAO.IDAO;

import com.lp2.lp2.Model.NegociacaoProposta;
import java.sql.SQLException;
import java.util.List;

public interface INegociacaoPropostaDAO {
    void addProposta(NegociacaoProposta proposta) throws SQLException;
    void updateProposta(NegociacaoProposta proposta) throws SQLException;
    List<NegociacaoProposta> getPropostasByLeilao(int leilaoId) throws SQLException;
}