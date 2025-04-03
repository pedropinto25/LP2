package com.lp2.lp2.Controller.Leilao;

import com.lp2.lp2.DAO.LeilaoDAO;
import com.lp2.lp2.DAO.LeilaoParticipacaoDAO;
import com.lp2.lp2.Model.Leilao;
import com.lp2.lp2.Model.LeilaoParticipacao;
import com.lp2.lp2.Util.LoaderFXML;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class ParticipateLeilaoController {

    @FXML
    private TableView<Leilao> leilaoTableView;
    @FXML
    private TableColumn<Leilao, Integer> idColumn;
    @FXML
    private TableColumn<Leilao, String> nomeColumn;
    @FXML
    private TableColumn<Leilao, String> descricaoColumn;
    @FXML
    private TableColumn<Leilao, String> tipoColumn;
    @FXML
    private TableColumn<Leilao, String> dataInicioColumn;
    @FXML
    private TableColumn<Leilao, String> dataFimColumn;
    @FXML
    private TableColumn<Leilao, String> valorMinimoColumn;
    @FXML
    private TableColumn<Leilao, String> valorMaximoColumn;
    @FXML
    private TableColumn<Leilao, String> multiploLanceColumn;
    @FXML
    private TableColumn<Leilao, Boolean> inativoColumn;

    @FXML
    private Button btnParticipar;
    @FXML
    private Button btnBack;
    @FXML
    private TextField valorLanceField;

    private LeilaoDAO leilaoDAO;
    private LeilaoParticipacaoDAO leilaoParticipacaoDAO;

    public ParticipateLeilaoController() throws SQLException {
        leilaoDAO = new LeilaoDAO();
        leilaoParticipacaoDAO = new LeilaoParticipacaoDAO();
    }

    @FXML
    private void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomeColumn.setCellValueFactory(new PropertyValueFactory<>("nome"));
        descricaoColumn.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        tipoColumn.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        dataInicioColumn.setCellValueFactory(new PropertyValueFactory<>("dataInicio"));
        dataFimColumn.setCellValueFactory(new PropertyValueFactory<>("dataFim"));
        valorMinimoColumn.setCellValueFactory(new PropertyValueFactory<>("valorMinimo"));
        valorMaximoColumn.setCellValueFactory(new PropertyValueFactory<>("valorMaximo"));
        multiploLanceColumn.setCellValueFactory(new PropertyValueFactory<>("multiploLance"));
        inativoColumn.setCellValueFactory(new PropertyValueFactory<>("inativo"));

        leilaoTableView.setItems(loadLeiloes());
    }

    private ObservableList<Leilao> loadLeiloes() {
        try {
            return FXCollections.observableArrayList(leilaoDAO.getAllLeiloes());
        } catch (SQLException e) {
            mostrarMensagemErro("Erro ao carregar leilões: " + e.getMessage());
            return FXCollections.observableArrayList();
        }
    }

    @FXML
    void handleBtnParticipar(ActionEvent event) {
        Leilao selectedLeilao = leilaoTableView.getSelectionModel().getSelectedItem();

        if (selectedLeilao != null) {
            try {
                BigDecimal valorLance = new BigDecimal(valorLanceField.getText());
                LeilaoParticipacao participacao = new LeilaoParticipacao();
                participacao.setLeilaoId(selectedLeilao.getId());
                participacao.setClienteId(1); // Substituir pelo ID do cliente logado
                participacao.setValorLance(valorLance);

                if ("Online".equals(selectedLeilao.getTipo())) {
                    // Lógica para um leilão online / eletrónico
                    // Verificar se o valor do lance é múltiplo do valor configurado
                    if (valorLance.remainder(selectedLeilao.getMultiploLance()).compareTo(BigDecimal.ZERO) != 0) {
                        mostrarMensagemErro("O valor do lance deve ser múltiplo de " + selectedLeilao.getMultiploLance());
                        return;
                    }
                    // Verificar se o valor do lance é igual ao valor mínimo
                    if (valorLance.compareTo(selectedLeilao.getValorMinimo()) == 0) {
                        mostrarMensagemErro("O valor do lance não pode ser igual ao valor mínimo.");
                        return;
                    }
                    // Atualizar o valor mínimo do leilão
                    selectedLeilao.setValorMinimo(valorLance);
                    leilaoDAO.updateLeilao(selectedLeilao);
                } else if ("Carta Fechada".equals(selectedLeilao.getTipo())) {
                    // Lógica para leilão de carta fechada
                    // Cada cliente só pode participar uma vez!
                    List<LeilaoParticipacao> participacoes = leilaoParticipacaoDAO.getParticipacoesByLeilaoId(selectedLeilao.getId());
                    for (LeilaoParticipacao p : participacoes) {
                        if (p.getClienteId() == 1) { // Substituir pelo ID do cliente logado
                            mostrarMensagemErro("Você já participou neste leilão.");
                            return;
                        }
                    }
                    // Verificar se o valor do lance é maior ou igual ao valor mínimo
                    if (valorLance.compareTo(selectedLeilao.getValorMinimo()) < 0) {
                        mostrarMensagemErro("O valor do lance deve ser maior ou igual ao valor mínimo de " + selectedLeilao.getValorMinimo());
                        return;
                    }
                } else if ("Venda Direta".equals(selectedLeilao.getTipo())) {
                    // Lógica para leilão venda direta
                    // O valor do lance deve ser igual ao valor mínimo
                    if (valorLance.compareTo(selectedLeilao.getValorMinimo()) != 0) {
                        mostrarMensagemErro("O valor do lance deve ser igual ao valor mínimo de " + selectedLeilao.getValorMinimo());
                        return;
                    }
                    // Verificar se o leilão já foi vendido
                    List<LeilaoParticipacao> participacoes = leilaoParticipacaoDAO.getParticipacoesByLeilaoId(selectedLeilao.getId());
                    if (!participacoes.isEmpty()) {
                        mostrarMensagemErro("Venda já feita.");
                        return;
                    }
                }

                leilaoParticipacaoDAO.addParticipacao(participacao);
                mostrarMensagemSucesso("Participação registrada com sucesso!");
            } catch (SQLException e) {
                mostrarMensagemErro("Erro ao registrar participação: " + e.getMessage());
            } catch (NumberFormatException e) {
                mostrarMensagemErro("Valor do lance inválido.");
            }
        } else {
            mostrarMensagemErro("Por favor, selecione um leilão antes de participar.");
        }
    }

    private void mostrarMensagemSucesso(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sucesso");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void mostrarMensagemErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    @FXML
    void handleBtnBack(ActionEvent actionEvent) {
        Stage currentStage = (Stage) btnBack.getScene().getWindow();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadMainMenu();
    }

    @FXML
    void handleBtnMenu(ActionEvent event) {
        // Implementar lógica para o botão de menu
    }
}