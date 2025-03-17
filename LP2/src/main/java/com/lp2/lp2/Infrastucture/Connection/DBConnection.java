package com.lp2.lp2.Infrastucture.Connection;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.SQLException;

public class DBConnection {

    private static HikariDataSource dataSource;

    static {
        Dotenv dotenv = Dotenv.load();
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(dotenv.get("DB_URL"));
        config.setUsername(dotenv.get("DB_USER"));
        config.setPassword(dotenv.get("DB_PASSWORD"));
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        dataSource = new HikariDataSource(config);

        // Mensagem de debug para verificar a configuração da conexão
        System.out.println("Configuração da conexão estabelecida com sucesso.");
    }

    public static Connection getConnection() throws SQLException {
        Connection connection = dataSource.getConnection();

        // Mensagem de debug para verificar se a conexão foi obtida
        if (connection != null) {
            System.out.println("Conexão obtida com sucesso.");
        } else {
            System.out.println("Falha ao obter conexão.");
        }

        return connection;
    }

    public static void closeDataSource() {
        if (dataSource != null) {
            dataSource.close();
            // Mensagem de debug para verificar se o DataSource foi fechado
            System.out.println("DataSource fechado com sucesso.");
        }
    }
}