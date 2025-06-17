package com.lp2.lp2.DAO;

import com.lp2.lp2.DAO.IDAO.ILeilaoParticipacaoDAO;
import com.lp2.lp2.Model.Leilao;
import com.lp2.lp2.Model.LeilaoParticipacao;
import com.lp2.lp2.Infrastucture.Connection.DBConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LeilaoParticipacaoDAO implements ILeilaoParticipacaoDAO {
    private Connection connection;

    public LeilaoParticipacaoDAO() throws SQLException {
        connection = DBConnection.getConnection();
    }

    @Override
    public void addParticipacao(LeilaoParticipacao participacao) throws SQLException {
        String sql = "INSERT INTO LeilaoParticipacao (leilao_id, cliente_id, valor_lance) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, participacao.getLeilaoId());
            stmt.setInt(2, participacao.getClienteId());
            stmt.setBigDecimal(3, participacao.getValorLance());
            stmt.executeUpdate();

            // Recuperar o ID gerado
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int generatedId = generatedKeys.getInt(1);
                    participacao.setId(generatedId); // Define o ID no objeto LeilaoParticipacao
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao adicionar participação: " + e.getMessage());
            throw e; // Re-lançando a exceção para o chamador
        }
    }

    @Override
    public void updateParticipacao(LeilaoParticipacao participacao) throws SQLException {
        String sql = "UPDATE LeilaoParticipacao SET leilao_id = ?, cliente_id = ?, data_participacao = ?, valor_lance = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, participacao.getLeilaoId());
            stmt.setInt(2, participacao.getClienteId());
            stmt.setTimestamp(3, participacao.getDataParticipacao());
            stmt.setBigDecimal(4, participacao.getValorLance());
            stmt.setInt(5, participacao.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar participação: " + e.getMessage());
            throw e; // Re-lançando a exceção para o chamador
        }
    }

    @Override
    public void deleteParticipacao(int id) throws SQLException {
        String sql = "DELETE FROM LeilaoParticipacao WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao deletar participação: " + e.getMessage());
            throw e; // Re-lançando a exceção para o chamador
        }
    }

    @Override
    public LeilaoParticipacao getParticipacaoById(int id) throws SQLException {
        String sql = "SELECT * FROM LeilaoParticipacao WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    LeilaoParticipacao participacao = new LeilaoParticipacao();
                    participacao.setId(rs.getInt("id"));
                    participacao.setLeilaoId(rs.getInt("leilao_id"));
                    participacao.setClienteId(rs.getInt("cliente_id"));
                    participacao.setDataParticipacao(rs.getTimestamp("data_participacao"));
                    participacao.setValorLance(rs.getBigDecimal("valor_lance"));
                    return participacao;
                }
            }
        }
        return null;
    }

    @Override
    public List<LeilaoParticipacao> getParticipacoesByLeilaoId(int leilaoId) throws SQLException {
        List<LeilaoParticipacao> participacoes = new ArrayList<>();
        String sql = "SELECT * FROM LeilaoParticipacao WHERE leilao_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, leilaoId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    LeilaoParticipacao participacao = new LeilaoParticipacao();
                    participacao.setId(rs.getInt("id"));
                    participacao.setLeilaoId(rs.getInt("leilao_id"));
                    participacao.setClienteId(rs.getInt("cliente_id"));
                    participacao.setDataParticipacao(rs.getTimestamp("data_participacao"));
                    participacao.setValorLance(rs.getBigDecimal("valor_lance"));
                    participacoes.add(participacao);
                }
            }
        }
        return participacoes;
    }

    @Override
    public List<LeilaoParticipacao> getAllParticipacoes() throws SQLException {
        List<LeilaoParticipacao> participacoes = new ArrayList<>();
        String sql = "SELECT * FROM LeilaoParticipacao";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                LeilaoParticipacao participacao = new LeilaoParticipacao();
                participacao.setId(rs.getInt("id"));
                participacao.setLeilaoId(rs.getInt("leilao_id"));
                participacao.setClienteId(rs.getInt("cliente_id"));
                participacao.setDataParticipacao(rs.getTimestamp("data_participacao"));
                participacao.setValorLance(rs.getBigDecimal("valor_lance"));
                participacoes.add(participacao);
            }
        }
        return participacoes;
    }

    @Override
    public List<LeilaoParticipacao> getClientesComMaisLancesPorLeilao() {
        List<LeilaoParticipacao> clientesLances = new ArrayList<>();
        String sql = "SELECT lp.leilao_id, lp.cliente_id, COUNT(lp.id) AS total_lances, MAX(lp.valor_lance) AS maior_lance " +
                "FROM LeilaoParticipacao lp " +
                "GROUP BY lp.leilao_id, lp.cliente_id " +
                "ORDER BY lp.leilao_id, maior_lance DESC";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int leilaoId = rs.getInt("leilao_id");
                int clienteId = rs.getInt("cliente_id");
                BigDecimal maiorLance = rs.getBigDecimal("maior_lance");

                LeilaoParticipacao clienteLances = new LeilaoParticipacao();
                clienteLances.setLeilaoId(leilaoId);
                clienteLances.setClienteId(clienteId);
                clienteLances.setValorLance(maiorLance);

                clientesLances.add(clienteLances);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return clientesLances;
    }
    @Override
    public int contarLeiloesInativos() {
        String sql = "SELECT COUNT(*) AS total FROM Leilao WHERE inativo = 1";
        int totalInativos = 0;

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                totalInativos = rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return totalInativos;
    }

    @Override
    public List<LeilaoParticipacao> getLeiloesComMaisLances() {
        List<LeilaoParticipacao> leiloesMaisLances = new ArrayList<>();
        String sql = "SELECT lp.leilao_id, COUNT(lp.id) AS total_lances " +
                "FROM LeilaoParticipacao lp " +
                "GROUP BY lp.leilao_id " +
                "ORDER BY total_lances DESC";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            int maxLances = -1;

            while (rs.next()) {
                int leilaoId = rs.getInt("leilao_id");
                int totalLances = rs.getInt("total_lances");

                // Apenas adiciona se for o primeiro ou igual ao máximo atual
                if (maxLances == -1 || totalLances == maxLances) {
                    LeilaoParticipacao leilao = new LeilaoParticipacao();
                    leilao.setLeilaoId(leilaoId);
                    leilao.setTotalLances(totalLances); // este método deve ser adicionado no teu modelo
                    leiloesMaisLances.add(leilao);

                    maxLances = totalLances;
                } else {
                    // Se já passou do máximo, termina
                    break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return leiloesMaisLances;
    }

    @Override
    public Leilao getLeilaoMaisTempoAtivo() {
        String sql = "SELECT TOP 1 *, DATEDIFF(DAY, dataInicio, dataFim) AS dias_ativos " +
                "FROM Leilao " +
                "WHERE dataInicio IS NOT NULL AND dataFim IS NOT NULL " +
                "ORDER BY dias_ativos DESC";

        Leilao leilaoMaisAtivo = null;

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                leilaoMaisAtivo = new Leilao();
                leilaoMaisAtivo.setId(rs.getInt("id"));
                leilaoMaisAtivo.setNome(rs.getString("nome"));
                leilaoMaisAtivo.setDescricao(rs.getString("descricao"));
                leilaoMaisAtivo.setTipo(rs.getString("tipo"));
                leilaoMaisAtivo.setDataInicio(rs.getDate("dataInicio"));
                leilaoMaisAtivo.setDataFim(rs.getDate("dataFim"));
                leilaoMaisAtivo.setValorMinimo(rs.getBigDecimal("valorMinimo"));
                leilaoMaisAtivo.setValorMaximo(rs.getBigDecimal("valorMaximo"));
                leilaoMaisAtivo.setMultiploLance(rs.getBigDecimal("multiploLance"));
                leilaoMaisAtivo.setInativo(rs.getBoolean("inativo"));
                leilaoMaisAtivo.setVendido(rs.getBoolean("vendido"));
                leilaoMaisAtivo.setDiasAtivos(rs.getInt("dias_ativos")); // precisa criar o campo na classe
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return leilaoMaisAtivo;
    }

    public Double getMediaIdadeClientes() {
        String sql = "SELECT AVG(DATEDIFF(YEAR, dataNascimento, GETDATE())) AS media_idade FROM Cliente WHERE dataNascimento IS NOT NULL";
        Double mediaIdade = null;

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                mediaIdade = rs.getDouble("media_idade");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return mediaIdade;
    }

    public Double getPercentagemMaiorDominioEmail() {
        String sql =
                "WITH DominioContagem AS ( " +
                        "    SELECT RIGHT(email, LEN(email) - CHARINDEX('@', email)) AS dominio, " +
                        "           COUNT(*) AS contagem " +
                        "    FROM Cliente " +
                        "    WHERE email IS NOT NULL AND email LIKE '%@%' " +
                        "    GROUP BY RIGHT(email, LEN(email) - CHARINDEX('@', email)) " +
                        "), " +
                        "MaxContagem AS ( " +
                        "    SELECT MAX(contagem) AS max_contagem FROM DominioContagem " +
                        "), " +
                        "TotalClientes AS ( " +
                        "    SELECT COUNT(*) AS total " +
                        "    FROM Cliente " +
                        "    WHERE email IS NOT NULL AND email LIKE '%@%' " +
                        ") " +
                        "SELECT CAST(mc.max_contagem * 100.0 / tc.total AS DECIMAL(5,2)) AS percentagem " +
                        "FROM MaxContagem mc CROSS JOIN TotalClientes tc";


        Double percentagem = null;

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                percentagem = rs.getDouble("percentagem");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return percentagem;
    }

    public int getTotalClientes() {
        String sql = "SELECT COUNT(*) AS total_clientes FROM Cliente";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt("total_clientes");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public int getTotalLeiloesSemLances() {
        String sql = "SELECT COUNT(*) AS total_leiloes_sem_lances " +
                "FROM Leilao l " +
                "LEFT JOIN LeilaoParticipacao p ON l.id = p.leilao_id " +
                "WHERE p.leilao_id IS NULL";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt("total_leiloes_sem_lances");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }


    public Double getMediaTempoPrimeiroLance() {
        String sql = "SELECT AVG(DATEDIFF(MINUTE, l.dataInicio, p.primeiro_lance)) AS media_minutos_primeiro_lance " +
                "FROM Leilao l " +
                "JOIN ( " +
                "    SELECT leilao_id, MIN(data_participacao) AS primeiro_lance " +
                "    FROM LeilaoParticipacao " +
                "    GROUP BY leilao_id " +
                ") p ON l.id = p.leilao_id " +
                "WHERE l.dataInicio IS NOT NULL";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getDouble("media_minutos_primeiro_lance");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }







}