package com.lp2.lp2.Util;

import com.lp2.lp2.Model.Cliente;
import com.lp2.lp2.Model.Lance;
import com.lp2.lp2.Model.Leilao;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.CSVWriterBuilder;
import com.opencsv.exceptions.CsvException;

import java.io.*;
import java.util.List;

public class CsvService {

    private static final String CSV_FILE_PATH = "src/main/java/com/lp2/lp2/Util/cliente.csv";
    private static final String[] CSV_HEADER = {"ID", "Nome", "Morada", "DataNascimento", "Email", "Senha"};

    private static final String LANCE_CSV_PATH = "src/main/java/com/lp2/lp2/Util/lance.csv";
    private static final String[] LANCE_HEADER = {"ID", "Valor", "DataHora", "ClienteID", "LeilaoID"};

    private static final String LEILAO_CSV_PATH = "src/main/java/com/lp2/lp2/Util/leilao.csv";
    private static final String[] LEILAO_HEADER = {"ID", "Nome", "Descricao", "Tipo", "DataInicio", "DataFim", "ValorMinimo", "ValorMaximo", "MultiploLance"};

    public void saveClienteToCsv(Cliente cliente) throws IOException {
        File csvFile = new File(CSV_FILE_PATH);
        boolean fileExists = csvFile.exists();

        try (CSVWriter writer = (CSVWriter) new CSVWriterBuilder(new FileWriter(CSV_FILE_PATH, true))
                .withSeparator(',')
                .withQuoteChar(CSVWriter.NO_QUOTE_CHARACTER)
                .build()) {

            // Se o arquivo não existir, cria o arquivo com o cabeçalho
            if (!fileExists) {
                writer.writeNext(CSV_HEADER);
            }

            // Remover espaços extras e garantir organização
            String[] data = {
                    String.valueOf(cliente.getId()).trim(),
                    cliente.getNome().trim(),
                    cliente.getMorada().trim(),
                    cliente.getDataNascimento().toString().trim(),
                    cliente.getEmail().trim(),
                    cliente.getSenha().trim()
            };
            writer.writeNext(data);
        }
    }

    public void updateClienteInCsv(Cliente cliente) throws IOException, CsvException {
        File csvFile = new File(CSV_FILE_PATH);
        if (!csvFile.exists()) {
            throw new FileNotFoundException("Arquivo CSV não encontrado");
        }

        List<String[]> allClients;
        try (CSVReader reader = new CSVReader(new FileReader(CSV_FILE_PATH))) {
            allClients = reader.readAll();
        }

        for (int i = 1; i < allClients.size(); i++) { // Começa em 1 para pular o cabeçalho
            String[] row = allClients.get(i);
            if (row[0].equals(String.valueOf(cliente.getId()))) {
                allClients.set(i, new String[]{
                        String.valueOf(cliente.getId()),
                        cliente.getNome().trim(),
                        cliente.getMorada().trim(),
                        cliente.getDataNascimento().toString().trim(),
                        cliente.getEmail().trim(),
                        cliente.getSenha().trim()
                });
                break;
            }
        }

        try (CSVWriter writer = (CSVWriter) new CSVWriterBuilder(new FileWriter(CSV_FILE_PATH))
                .withSeparator(',')
                .withQuoteChar(CSVWriter.NO_QUOTE_CHARACTER)
                .build()) {
            writer.writeAll(allClients);
        }
    }
    public void saveLanceToCsv(Lance lance) throws IOException {
        File csvFile = new File(LANCE_CSV_PATH);
        boolean fileExists = csvFile.exists();

        try (CSVWriter writer = (CSVWriter) new CSVWriterBuilder(new FileWriter(LANCE_CSV_PATH, true))
                .withSeparator(',')
                .withQuoteChar(CSVWriter.NO_QUOTE_CHARACTER)
                .build()) {

            if (!fileExists) {
                writer.writeNext(LANCE_HEADER);
            }

            String[] data = {
                    String.valueOf(lance.getId()).trim(),
                    lance.getValor().toString().trim(),
                    lance.getDataHora().toString().trim(),
                    String.valueOf(lance.getClienteId()).trim(),
                    String.valueOf(lance.getLeilaoId()).trim()
            };
            writer.writeNext(data);
        }
    }

    public void updateLanceInCsv(Lance lance) throws IOException, CsvException {
        File csvFile = new File(LANCE_CSV_PATH);
        if (!csvFile.exists()) {
            throw new FileNotFoundException("Arquivo CSV não encontrado");
        }

        List<String[]> allLances;
        try (CSVReader reader = new CSVReader(new FileReader(LANCE_CSV_PATH))) {
            allLances = reader.readAll();
        }

        for (int i = 1; i < allLances.size(); i++) {
            String[] row = allLances.get(i);
            if (row[0].equals(String.valueOf(lance.getId()))) {
                allLances.set(i, new String[]{
                        String.valueOf(lance.getId()),
                        lance.getValor().toString().trim(),
                        lance.getDataHora().toString().trim(),
                        String.valueOf(lance.getClienteId()).trim(),
                        String.valueOf(lance.getLeilaoId()).trim()
                });
                break;
            }
        }

        try (CSVWriter writer = (CSVWriter) new CSVWriterBuilder(new FileWriter(LANCE_CSV_PATH))
                .withSeparator(',')
                .withQuoteChar(CSVWriter.NO_QUOTE_CHARACTER)
                .build()) {
            writer.writeAll(allLances);
        }
    }

    public void saveLeilaoToCsv(Leilao leilao) throws IOException {
        File csvFile = new File(LEILAO_CSV_PATH);
        boolean fileExists = csvFile.exists();

        try (CSVWriter writer = (CSVWriter) new CSVWriterBuilder(new FileWriter(LEILAO_CSV_PATH, true))
                .withSeparator(',')
                .withQuoteChar(CSVWriter.NO_QUOTE_CHARACTER)
                .build()) {

            if (!fileExists) {
                writer.writeNext(LEILAO_HEADER);
            }

            String[] data = {
                    String.valueOf(leilao.getId()).trim(),
                    leilao.getNome().trim(),
                    leilao.getDescricao().trim(),
                    leilao.getTipo().trim(),
                    leilao.getDataInicio().toString().trim(),
                    leilao.getDataFim().toString().trim(),
                    leilao.getValorMinimo().toString().trim(),
                    leilao.getValorMaximo().toString().trim(),
                    leilao.getMultiploLance().toString().trim(),
                    String.valueOf(leilao.getInativo()).trim()
            };
            writer.writeNext(data);
        }
    }

    public void updateLeilaoInCsv(Leilao leilao) throws IOException, CsvException {
        File csvFile = new File(LEILAO_CSV_PATH);
        if (!csvFile.exists()) {
            throw new FileNotFoundException("Arquivo CSV não encontrado");
        }

        List<String[]> allLeiloes;
        try (CSVReader reader = new CSVReader(new FileReader(LEILAO_CSV_PATH))) {
            allLeiloes = reader.readAll();
        }

        for (int i = 1; i < allLeiloes.size(); i++) {
            String[] row = allLeiloes.get(i);
            if (row[0].equals(String.valueOf(leilao.getId()))) {
                allLeiloes.set(i, new String[]{
                        String.valueOf(leilao.getId()),
                        leilao.getNome().trim(),
                        leilao.getDescricao().trim(),
                        leilao.getTipo().trim(),
                        leilao.getDataInicio().toString().trim(),
                        leilao.getDataFim().toString().trim(),
                        leilao.getValorMinimo().toString().trim(),
                        leilao.getValorMaximo().toString().trim(),
                        leilao.getMultiploLance().toString().trim(),
                        String.valueOf(leilao.getInativo()).trim()
                });
                break;
            }
        }

        try (CSVWriter writer = (CSVWriter) new CSVWriterBuilder(new FileWriter(LEILAO_CSV_PATH))
                .withSeparator(',')
                .withQuoteChar(CSVWriter.NO_QUOTE_CHARACTER)
                .build()) {
            writer.writeAll(allLeiloes);
        }
    }
}


