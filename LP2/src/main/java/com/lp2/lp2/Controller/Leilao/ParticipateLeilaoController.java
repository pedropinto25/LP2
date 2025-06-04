package com.lp2.lp2.Controller.Leilao;

import com.lp2.lp2.DAO.ClienteDAO;
import com.lp2.lp2.DAO.LeilaoDAO;
import com.lp2.lp2.DAO.LeilaoParticipacaoDAO;
import com.lp2.lp2.DAO.PontosDAO;
import com.lp2.lp2.Model.Cliente;
import com.lp2.lp2.Model.Leilao;
import com.lp2.lp2.Model.LeilaoParticipacao;
import com.lp2.lp2.Session.Session;
import com.lp2.lp2.Util.GmailSender;
import com.lp2.lp2.Util.LoaderFXML;
import io.github.cdimascio.dotenv.Dotenv;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import com.lp2.lp2.Service.EmailNotificationService;




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
    TableColumn<Leilao, String> tipoColumn;
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
    private TableColumn<Leilao, Boolean> vendidoColumn;

    @FXML
    private Button btnParticipar;
    @FXML
    private Button btnBack;
    @FXML
    private TextField valorLanceField;
    @FXML
    private Button btnTerminar;
    @FXML
    private Label pontosLabel;
    @FXML
    private Button btnAddPontos;

    private LeilaoDAO leilaoDAO;
    private LeilaoParticipacaoDAO leilaoParticipacaoDAO;
    private PontosDAO pontosDAO;
    private ClienteDAO clienteDAO;
    private static final Dotenv dotenv = Dotenv.load();

    private EmailNotificationService emailNotificationService;

    public ParticipateLeilaoController() throws SQLException {
        leilaoDAO = new LeilaoDAO();
        leilaoParticipacaoDAO = new LeilaoParticipacaoDAO();
        pontosDAO = new PontosDAO();
        emailNotificationService = new EmailNotificationService();
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
        vendidoColumn.setCellValueFactory(new PropertyValueFactory<>("vendido"));
        leilaoTableView.setItems(loadLeiloes());

        // Verificar leilões com data final
        verificarLeiloesComDataFinal();

        // Atualizar pontos do cliente logado
        atualizarPontosCliente();

        leilaoTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                if ("Online".equals(newSelection.getTipo())) {
                    valorLanceField.setDisable(true); // Desativa o campo
                    valorLanceField.clear(); // Opcional: limpa o campo!
                } else {
                    valorLanceField.setDisable(false); // Ativa o campo
                }
            } else {
                valorLanceField.setDisable(false);
            }
        });

    }

    private ObservableList<Leilao> loadLeiloes() {
        try {
            return FXCollections.observableArrayList(leilaoDAO.getAllLeiloes());
        } catch (SQLException e) {
            mostrarMensagemErro("Erro ao carregar leilões: " + e.getMessage());
            return FXCollections.observableArrayList();
        }
    }

    private void atualizarPontosCliente() {
        try {
            int pontos = pontosDAO.verificarPontos(Session.getLoggedUserId());
            pontosLabel.setText("Créditos: " + pontos);
        } catch (SQLException e) {
            mostrarMensagemErro("Erro ao carregar créditos do cliente: " + e.getMessage());
        }
    }

    @FXML
    void handleBtnParticipar(ActionEvent event) {
        Leilao selectedLeilao = leilaoTableView.getSelectionModel().getSelectedItem();

        if (selectedLeilao != null) {
            if (selectedLeilao.getVendido()) {
                mostrarMensagemErro("Este leilão já foi vendido e não aceita mais lances.");
                return;
            }

            try {
                int clienteId = Session.getLoggedUserId();
                LeilaoParticipacao participacao = new LeilaoParticipacao();
                participacao.setLeilaoId(selectedLeilao.getId());
                participacao.setClienteId(clienteId);

                if ("Online".equals(selectedLeilao.getTipo())) {
                    // Substituir validação anterior por esta:
                    BigDecimal multiploLance = selectedLeilao.getMultiploLance();
                    BigDecimal pontos = new BigDecimal(pontosDAO.verificarPontos(clienteId)); // Assume que retorna int ou BigDecimal

                    if (pontos.compareTo(multiploLance) < 0) {
                        mostrarMensagemErro("Você não tem créditos suficientes para cobrir o múltiplo de lance (" + multiploLance + ").");
                        return;
                    }

                    // Validar cliente não ser o último a licitar
                    List<LeilaoParticipacao> participacoes = leilaoParticipacaoDAO.getParticipacoesByLeilaoId(selectedLeilao.getId());
                    if (!participacoes.isEmpty()) {
                        participacoes.sort((p1, p2) -> p2.getDataParticipacao().compareTo(p1.getDataParticipacao()));
                        LeilaoParticipacao ultimaParticipacao = participacoes.get(0);
                        if (ultimaParticipacao.getClienteId() == clienteId) {
                            mostrarMensagemErro("Você já possui o último lance neste leilão. Aguarde outro participante!");
                            return;
                        }
                    }

                    BigDecimal valorMinimo = selectedLeilao.getValorMinimo();
                    BigDecimal valorLance = valorMinimo.add(multiploLance);

                    if (selectedLeilao.getValorMaximo() != null) {
                        if (valorLance.compareTo(selectedLeilao.getValorMaximo()) == 0) {
                            selectedLeilao.setValorMinimo(valorLance);
                            selectedLeilao.setVendido(true);
                            leilaoDAO.updateLeilao(selectedLeilao);
                            mostrarMensagemSucesso("Leilão vendido pelo valor máximo!");
                            // Envio de e-mail via serviço
                            String email = getEmailByUserId(clienteId);
                            String nome = getUserNameById(clienteId);
                            emailNotificationService.enviarEmailVencedorLeilao(email, nome, selectedLeilao.getId(), selectedLeilao.getNome());
                            return;
                        } else if (valorLance.compareTo(selectedLeilao.getValorMaximo()) > 0) {
                            mostrarMensagemErro("Não pode licitar acima do valor máximo de " + selectedLeilao.getValorMaximo());
                            return;
                        }
                    }

                    participacao.setValorLance(valorLance);
                    selectedLeilao.setValorMinimo(valorLance);
                    leilaoDAO.updateLeilao(selectedLeilao);
                    leilaoParticipacaoDAO.addParticipacao(participacao);

                    // Tira créditos no valor do múltiplo de lance
                    pontosDAO.removerPontos(clienteId, multiploLance.intValue());
                    atualizarPontosCliente();

                    mostrarMensagemSucesso("Lance efetuado com sucesso! Novo valor mínimo: " + valorLance);

                } else if ("Carta Fechada".equals(selectedLeilao.getTipo())) {
                    // Lógica já existente para carta fechada (mantém igual)
                    BigDecimal valorLance = new BigDecimal(valorLanceField.getText());
                    participacao.setValorLance(valorLance);

                    List<LeilaoParticipacao> participacoes = leilaoParticipacaoDAO.getParticipacoesByLeilaoId(selectedLeilao.getId());
                    for (LeilaoParticipacao p : participacoes) {
                        if (p.getClienteId() == clienteId) {
                            mostrarMensagemErro("Você já participou neste leilão.");
                            return;
                        }
                    }
                    if (valorLance.compareTo(selectedLeilao.getValorMinimo()) < 0) {
                        mostrarMensagemErro("O valor do lance deve ser maior ou igual ao valor mínimo de " + selectedLeilao.getValorMinimo());
                        return;
                    }

                    leilaoParticipacaoDAO.addParticipacao(participacao);

                } else if ("Venda Direta".equals(selectedLeilao.getTipo())) {
                    BigDecimal valorLance = new BigDecimal(valorLanceField.getText());
                    participacao.setValorLance(valorLance);

                    if (selectedLeilao.getValorMinimo() != null && valorLance.compareTo(selectedLeilao.getValorMinimo()) != 0) {
                        mostrarMensagemErro("O valor do lance deve ser igual ao valor mínimo de " + selectedLeilao.getValorMinimo());
                        return;
                    }
                    List<LeilaoParticipacao> participacoes = leilaoParticipacaoDAO.getParticipacoesByLeilaoId(selectedLeilao.getId());
                    if (!participacoes.isEmpty()) {
                        mostrarMensagemErro("Venda já feita.");
                        return;
                    }

                    selectedLeilao.setVendido(true);
                    leilaoDAO.updateLeilao(selectedLeilao);
                    leilaoParticipacaoDAO.addParticipacao(participacao);
                    mostrarMensagemSucesso("Leilão vendido pelo valor mínimo!");
                    String email = getEmailByUserId(clienteId);
                    String nome = getUserNameById(clienteId);
                    emailNotificationService.enviarEmailVencedorLeilao(email, nome, selectedLeilao.getId(), selectedLeilao.getNome());
                    return;

                }

                if (!"Online".equals(selectedLeilao.getTipo())) {
                    mostrarMensagemSucesso("Participação registrada com sucesso!");
                }
            } catch (SQLException e) {
                mostrarMensagemErro("Erro ao registrar participação: " + e.getMessage());
            } catch (NumberFormatException e) {
                mostrarMensagemErro("Valor do lance inválido.");
            }
        } else {
            mostrarMensagemErro("Por favor, selecione um leilão antes de participar.");
        }
    }



    @FXML
    void handleBtnTerminar(ActionEvent event) {
        Leilao selectedLeilao = leilaoTableView.getSelectionModel().getSelectedItem();

        if (selectedLeilao != null) {
            try {
                if ("Online".equals(selectedLeilao.getTipo()) || "Carta Fechada".equals(selectedLeilao.getTipo())) {
                    List<LeilaoParticipacao> participacoes = leilaoParticipacaoDAO.getParticipacoesByLeilaoId(selectedLeilao.getId());
                    if (!participacoes.isEmpty()) {
                        LeilaoParticipacao maiorParticipacao = participacoes.stream()
                                .max((p1, p2) -> p1.getValorLance().compareTo(p2.getValorLance()))
                                .orElse(null);

                        if (maiorParticipacao != null) {
                            selectedLeilao.setVendido(true);
                            leilaoDAO.updateLeilao(selectedLeilao);
                            mostrarMensagemSucesso("Leilão terminado e vendido ao cliente com o maior lance!\n" +
                                    "Valor vendido: " + maiorParticipacao.getValorLance() + "\n" +
                                    "Cliente ID: " + maiorParticipacao.getClienteId());


                            String email = getEmailByUserId(maiorParticipacao.getClienteId());
                            String nome = getUserNameById(maiorParticipacao.getClienteId());
                            emailNotificationService.enviarEmailVencedorLeilao(email, nome, selectedLeilao.getId(), selectedLeilao.getNome());



                        }
                    } else {
                        mostrarMensagemErro("Nenhuma participação encontrada para este leilão.");
                    }
                } else if ("Venda Direta".equals(selectedLeilao.getTipo())) {
                    selectedLeilao.setInativo(true);
                    leilaoDAO.updateLeilao(selectedLeilao);
                    mostrarMensagemSucesso("Leilão de Venda Direta terminado e marcado como inativo.");
                }
            } catch (SQLException e) {
                mostrarMensagemErro("Erro ao terminar leilão: " + e.getMessage());
            }
        } else {
            mostrarMensagemErro("Por favor, selecione um leilão antes de terminar.");
        }
    }

    public void verificarLeiloesComDataFinal() {
        try {
            List<Leilao> leiloes = leilaoDAO.getAllLeiloes();
            for (Leilao leilao : leiloes) {
                if (leilao.getDataFim() != null && leilao.getDataFim().before(new Date())) {
                    if ("Online".equals(leilao.getTipo()) || "Carta Fechada".equals(leilao.getTipo())) {
                        List<LeilaoParticipacao> participacoes = leilaoParticipacaoDAO.getParticipacoesByLeilaoId(leilao.getId());
                        if (!participacoes.isEmpty()) {
                            LeilaoParticipacao maiorParticipacao = participacoes.stream()
                                    .max((p1, p2) -> p1.getValorLance().compareTo(p2.getValorLance()))
                                    .orElse(null);

                            if (maiorParticipacao != null) {
                                leilao.setVendido(true);
                                leilaoDAO.updateLeilao(leilao);
                                mostrarMensagemSucesso("Leilão terminado e vendido ao cliente com o maior lance!\n" +
                                        "Valor vendido: " + maiorParticipacao.getValorLance() + "\n" +
                                        "Cliente ID: " + maiorParticipacao.getClienteId());



                                String email = getEmailByUserId(maiorParticipacao.getClienteId());
                                String nome = getUserNameById(maiorParticipacao.getClienteId());
                                emailNotificationService.enviarEmailVencedorLeilao(email, nome, leilao.getId(), leilao.getNome());



                            }
                        }
                    } else if ("Venda Direta".equals(leilao.getTipo())) {
                        leilao.setInativo(true);
                        leilaoDAO.updateLeilao(leilao);
                        mostrarMensagemSucesso("Leilão de Venda Direta " + leilao.getId() + " sem comprador! Foi terminado e marcado como inativo.");
                    }
                }
            }
        } catch (SQLException e) {
            mostrarMensagemErro("Erro ao verificar leilões com data final: " + e.getMessage());
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

    @FXML
    void handleBtnAddPontos(ActionEvent event) {
        try {
            Stage stage = new Stage();
            LoaderFXML loader = new LoaderFXML(stage);
            loader.loadAddPontos();
            // Atualizar pontos do cliente após fechar a janela
            stage.setOnHiding(windowEvent -> atualizarPontosCliente());
        } catch (Exception e) {
            mostrarMensagemErro("Erro ao abrir a janela de adicionar créditos: " + e.getMessage());
        }
    }

        private String getEmailByUserId(int id) throws SQLException {
        ClienteDAO clienteDAO = new ClienteDAO();
        Cliente cliente = clienteDAO.getClienteById(id);
        return cliente != null ? cliente.getEmail() : "Email não disponível";
    }

    private String getUserNameById(int id) throws SQLException {
        ClienteDAO clienteDAO = new ClienteDAO();
        Cliente cliente = clienteDAO.getClienteById(id);
        return cliente != null ? cliente.getNome() : "Nome não disponível";
    }

}
