package br.com.kelvin.persistence.config;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;



public class ConnectionConfig {

    private static final Properties properties = new Properties();

    static {
        try (InputStream input =  ConnectionConfig.class.getClassLoader()
                .getResourceAsStream("db.properties")) {

            if (input == null) {
                throw new RuntimeException("Arquivo db.properties não encontrado no classpath");
            }

            properties.load(input);

        } catch (IOException e) {
            throw new RuntimeException("Erro ao carregar configuração do banco de dados", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        Connection connection = DriverManager.getConnection(
                properties.getProperty("db.url"),
                properties.getProperty("db.username"),
                properties.getProperty("db.password")
        );

        connection.setAutoCommit(false);

        return connection;
    }
}
