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

        // Configuração da ligação
        config.setJdbcUrl(dotenv.get("DB_URL"));
        config.setUsername(dotenv.get("DB_USER"));
        config.setPassword(dotenv.get("DB_PASSWORD"));

        // Otimizações
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        // Configurações da pool
        config.setMaximumPoolSize(15);          // Máximo de conexões na pool
        config.setMinimumIdle(0);               // Mínimo de conexões ativas (0 = poupança máxima)
        config.setIdleTimeout(20000);           // Fecha conexões inativas após 30s
        config.setConnectionTimeout(15000);     // Espera no máximo 10s por uma nova conexão

        dataSource = new HikariDataSource(config);
        System.out.println("Configuração da conexão estabelecida com sucesso.");
    }

    public static Connection getConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
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
            System.out.println("DataSource fechado com sucesso.");
        }
    }
}
