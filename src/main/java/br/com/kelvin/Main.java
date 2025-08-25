package br.com.kelvin;

import br.com.kelvin.persistence.config.ConnectionFactory;
import br.com.kelvin.persistence.migrations.MigrationStrategy;
import br.com.kelvin.ui.InitialMenu;
import java.sql.Connection;


public class Main {

    public static void main(String[] args){
        MigrationStrategy migrationStrategy = new MigrationStrategy();
        migrationStrategy.executeMigration();
        try (Connection connection = ConnectionFactory.getConnection()) {
            InitialMenu menu = new InitialMenu(connection);
            menu.showMenu();
        } catch (Exception e) {
            System.err.println("Erro ao conectar ao banco de dados: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

