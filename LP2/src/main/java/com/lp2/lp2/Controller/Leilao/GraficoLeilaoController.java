package com.lp2.lp2.Controller.Leilao;

import com.lp2.lp2.DAO.LeilaoDAO;
import com.lp2.lp2.DAO.LeilaoParticipacaoDAO;
import com.lp2.lp2.Model.Leilao;
import com.lp2.lp2.Model.LeilaoParticipacao;
import com.lp2.lp2.Infrastucture.Connection.DBConnection;
import com.lp2.lp2.Util.LoaderFXML;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraficoLeilaoController {
    @FXML
    private BarChart<String, Number> barChart;
    @FXML
    private CategoryAxis xAxis;
    @FXML
    private NumberAxis yAxis;
    @FXML
    private Button btnLoadData;
    @FXML
    private Button btnLeiloesInativos;

    private LeilaoParticipacaoDAO leilaoParticipacaoDAO;


    public GraficoLeilaoController() throws SQLException {
        leilaoParticipacaoDAO = new LeilaoParticipacaoDAO();
    }
    @FXML
    public void initialize() {
        xAxis.setLabel("Leilão");
        yAxis.setLabel("Valor do Lance");
    }

    @FXML
    private void handleBtnLoadData() {
        System.out.println("Carregar Dados botão pressionado");
        loadClientesComMaisLances();
    }

    private void loadClientesComMaisLances() {
        List<LeilaoParticipacao> participacoes = leilaoParticipacaoDAO.getClientesComMaisLancesPorLeilao();
        System.out.println("Número de participações carregadas: " + participacoes.size());
        ObservableList<XYChart.Series<String, Number>> barChartData = FXCollections.observableArrayList();

        Map<Integer, XYChart.Series<String, Number>> seriesMap = new HashMap<>();

        for (LeilaoParticipacao lp : participacoes) {
            String leilaoId = String.valueOf(lp.getLeilaoId());
            BigDecimal maiorLance = lp.getValorLance();

            if (maiorLance != null) {
                System.out.println("Adicionando dados: Leilão ID = " + leilaoId + ", Cliente ID = " + lp.getClienteId() + ", Valor Lance = " + maiorLance);
                XYChart.Series<String, Number> series = seriesMap.getOrDefault(lp.getClienteId(), new XYChart.Series<>());
                series.setName("Cliente " + lp.getClienteId());
                XYChart.Data<String, Number> data = new XYChart.Data<>(leilaoId, maiorLance);

                // Adicionar Tooltip
                Tooltip tooltip = new Tooltip("Leilão ID: " + leilaoId + "\nCliente ID: " + lp.getClienteId() + "\nValor Lance: " + maiorLance);
                Tooltip.install(data.getNode(), tooltip);

                series.getData().add(data);
                seriesMap.put(lp.getClienteId(), series);
            } else {
                System.out.println("Valor de lance é nulo para Leilão ID = " + leilaoId + ", Cliente ID = " + lp.getClienteId());
            }
        }

        barChartData.addAll(seriesMap.values());
        barChart.setData(barChartData);
        System.out.println("Dados carregados no gráfico.");
    }

    @FXML
    private void handleBtnLeiloesInativos() {
        try {
            int totalInativos = leilaoParticipacaoDAO.contarLeiloesInativos(); // novo método no DAO
            barChart.getData().clear(); // limpa o gráfico

            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Leilões Inativos");

            XYChart.Data<String, Number> data = new XYChart.Data<>("Inativos", totalInativos);
            series.getData().add(data);

            barChart.getData().add(series);

            System.out.println("Total de leilões inativos: " + totalInativos);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBtnLeiloesMaisLances() {
        List<LeilaoParticipacao> leiloes = leilaoParticipacaoDAO.getLeiloesComMaisLances();

        if (!leiloes.isEmpty()) {
            barChart.getData().clear();

            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Leilões com mais lances");

            for (LeilaoParticipacao lp : leiloes) {
                String label = "Leilão " + lp.getLeilaoId();
                int total = lp.getTotalLances();

                XYChart.Data<String, Number> data = new XYChart.Data<>(label, total);
                series.getData().add(data);
            }

            barChart.getData().add(series);
            System.out.println("Leilões com mais lances carregados.");
        } else {
            System.out.println("Nenhum leilão com lances encontrado.");
        }
    }

    @FXML
    private void handleBtnLeilaoMaisTempoAtivo() {
        Leilao leilao = leilaoParticipacaoDAO.getLeilaoMaisTempoAtivo();

        if (leilao != null) {
            barChart.getData().clear();

            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Leilão com mais tempo ativo");

            XYChart.Data<String, Number> data = new XYChart.Data<>("Leilão " + leilao.getId(), leilao.getDiasAtivos());
            series.getData().add(data);

            barChart.getData().add(series);

            System.out.println("Leilão mais ativo: ID = " + leilao.getId() + ", Dias Ativo = " + leilao.getDiasAtivos());
        } else {
            System.out.println("Nenhum leilão com datas válidas encontrado.");
        }
    }

    @FXML
    private void handleBtnMediaIdadeClientes() {
        Double mediaIdade = leilaoParticipacaoDAO.getMediaIdadeClientes();

        if (mediaIdade != null) {
            barChart.getData().clear();

            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Média de Idade");

            XYChart.Data<String, Number> data = new XYChart.Data<>("Clientes", mediaIdade);
            series.getData().add(data);

            barChart.getData().add(series);

            System.out.println("Média de idade dos clientes: " + mediaIdade);
        } else {
            System.out.println("Não foi possível calcular a média de idade.");
        }
    }

    @FXML
    private void handleBtnPercentagemDominioEmail() {
        Double percentagem = leilaoParticipacaoDAO.getPercentagemMaiorDominioEmail();

        if (percentagem != null) {
            barChart.getData().clear();

            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Domínio Mais Usado");

            XYChart.Data<String, Number> data = new XYChart.Data<>("Percentagem", percentagem);
            series.getData().add(data);

            barChart.getData().add(series);

            System.out.println("Percentagem de clientes com o domínio mais comum: " + percentagem + "%");
        } else {
            System.out.println("Não foi possível calcular a percentagem.");
        }
    }
    @FXML
    private void handleBtnTotalClientes() {
        int totalClientes = leilaoParticipacaoDAO.getTotalClientes();

        barChart.getData().clear(); // Limpa os dados anteriores

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Total de Clientes");

        XYChart.Data<String, Number> data = new XYChart.Data<>("Total", totalClientes);
        series.getData().add(data);

        barChart.getData().add(series);

        System.out.println("Total de clientes registados: " + totalClientes);
    }

    @FXML
    private void handleBtnLeiloesSemLances() {
        int totalSemLances = leilaoParticipacaoDAO.getTotalLeiloesSemLances();

        barChart.getData().clear();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Leilões sem lances");

        XYChart.Data<String, Number> data = new XYChart.Data<>("Total", totalSemLances);
        series.getData().add(data);

        barChart.getData().add(series);

        System.out.println("Total de leilões sem lances: " + totalSemLances);
    }

    @FXML
    private void handleBtnMediaTempoPrimeiroLance() {
        Double mediaMinutos = leilaoParticipacaoDAO.getMediaTempoPrimeiroLance();

        if (mediaMinutos != null) {
            System.out.println("Média de tempo para o primeiro lance acontecer: " + mediaMinutos + " minutos");
            // Podes adicionar a um gráfico ou UI aqui
        } else {
            System.out.println("Não foi possível calcular a média.");
        }
    }





    @FXML
    private void handleBtnMenu() {
        Stage currentStage = (Stage) btnLoadData.getScene().getWindow();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadMainMenu();
    }

    @FXML
    private void handleBtnBack() {
        Stage currentStage = (Stage) btnLoadData.getScene().getWindow();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadMainMenu();
    }
}
