package com.lp2.lp2.Controller.Leilao;

import com.lp2.lp2.DAO.LeilaoDAO;
import com.lp2.lp2.DAO.LeilaoParticipacaoDAO;
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
