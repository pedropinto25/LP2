package com.lp2.lp2.Controller.Importador;

import com.lp2.lp2.DAO.ClienteDAO;
import com.lp2.lp2.Model.Cliente;

import com.lp2.lp2.DAO.LeilaoDAO;
import com.lp2.lp2.Model.Leilao;

import com.lp2.lp2.Util.LoaderFXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ImportadorController {

    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final LeilaoDAO leilaoDAO = new LeilaoDAO();

    private File ficheiroClientes;
    private File ficheiroLeiloes;

    @FXML
    private TextField txtFicheiroClientes;

    @FXML
    private TextField txtFicheiroLeiloes;

    @FXML
    private Button btnImportarClientes;

    @FXML
    private Button btnImportarLeiloes;

    @FXML
    private Button btnBack;

    public ImportadorController() throws SQLException {
    }

    @FXML
    void selecionarFicheiroClientes(ActionEvent event) {
        ficheiroClientes = escolherFicheiro();
        if (ficheiroClientes != null) {
            txtFicheiroClientes.setText(ficheiroClientes.getName());
        }
    }

    @FXML
    void selecionarFicheiroLeiloes(ActionEvent event) {
        ficheiroLeiloes = escolherFicheiro();
        if (ficheiroLeiloes != null) {
            txtFicheiroLeiloes.setText(ficheiroLeiloes.getName());
        }
    }

    @FXML
    void importarClientes(ActionEvent event) {
        if (ficheiroClientes == null) {
            mostrarAlerta("Por favor selecione um ficheiro de clientes.", Alert.AlertType.WARNING);
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(ficheiroClientes, java.nio.charset.StandardCharsets.ISO_8859_1))) {
            List<Cliente> clientes = new ArrayList<>();
            String cabecalho = br.readLine(); // ignora a primeira linha

            if (cabecalho == null) {
                throw new IllegalArgumentException("O ficheiro está vazio.");
            }

            String linha;
            int linhaNum = 1;
            while ((linha = br.readLine()) != null) {
                linhaNum++;
                String[] campos = linha.split(";");

                if (campos.length < 4) {
                    throw new IllegalArgumentException("Linha " + linhaNum + ": Faltam campos obrigatórios.");
                }

                Cliente c = new Cliente();
                c.setNome(campos[0]);
                c.setMorada(campos[1]);

                try {
                    // Converte formato yyyymmdd para yyyy-mm-dd
                    String dataStr = campos[2];
                    String dataFormatada = dataStr.substring(0,4) + "-" + dataStr.substring(4,6) + "-" + dataStr.substring(6,8);
                    c.setDataNascimento(Date.valueOf(dataFormatada));
                } catch (Exception e) {
                    throw new IllegalArgumentException("Linha " + linhaNum + ": Data inválida (" + campos[2] + ")");
                }

                c.setEmail(campos[3]);
                c.setSenha("123"); // senha padrão

                clientes.add(c);
            }

            clienteDAO.inserirClientes(clientes);
            mostrarAlerta("Clientes importados com sucesso", Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            mostrarAlerta("Erro: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }



    @FXML
    void importarLeiloes(ActionEvent event) {
        if (ficheiroLeiloes == null) {
            mostrarAlerta("Por favor selecione um ficheiro de leilões.", Alert.AlertType.WARNING);
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(ficheiroLeiloes, java.nio.charset.StandardCharsets.ISO_8859_1))) {
            List<Leilao> leiloes = new ArrayList<>();
            String cabecalho = br.readLine(); // ignora cabeçalho

            if (cabecalho == null) {
                throw new IllegalArgumentException("O ficheiro está vazio.");
            }

            String linha;
            int linhaNum = 1;
            while ((linha = br.readLine()) != null) {
                linhaNum++;
                String[] campos = linha.split(";");
                if (campos.length < 6) {
                    throw new IllegalArgumentException("Linha " + linhaNum + ": Faltam campos obrigatórios.");
                }

                Leilao l = new Leilao();
                l.setNome(campos[0]);
                l.setDescricao(campos[1]);
                l.setTipo(campos[2]); // deixamos como "1", "2", etc.

                try {
                    String dataInicio = campos[3];
                    String dataFim = campos[4];
                    l.setDataInicio(Date.valueOf(dataInicio.substring(0,4) + "-" + dataInicio.substring(4,6) + "-" + dataInicio.substring(6,8)));
                    l.setDataFim(Date.valueOf(dataFim.substring(0,4) + "-" + dataFim.substring(4,6) + "-" + dataFim.substring(6,8)));
                } catch (Exception e) {
                    throw new IllegalArgumentException("Linha " + linhaNum + ": Data inválida.");
                }

                try {
                    l.setValorMaximo(new BigDecimal(campos[5].replace(",", ".")));
                } catch (Exception e) {
                    throw new IllegalArgumentException("Linha " + linhaNum + ": Valor final inválido.");
                }

                // Campos não fornecidos no CSV
                l.setValorMinimo(null);
                l.setMultiploLance(null);

                leiloes.add(l);
            }

            leilaoDAO.inserirLeiloes(leiloes);
            mostrarAlerta("Leilões importados com sucesso", Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            mostrarAlerta("Erro: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }


    private File escolherFicheiro() {
        FileChooser fc = new FileChooser();
        fc.setTitle("Selecionar ficheiro CSV");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV", "*.csv"));
        return fc.showOpenDialog(new Stage());
    }

    private void mostrarAlerta(String mensagem, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle("Resultado da Importação");
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
}
