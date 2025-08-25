package br.com.kelvin;

import br.com.kelvin.persistence.config.ConnectionFactory;
import br.com.kelvin.persistence.dao.BlockDAO;
import br.com.kelvin.persistence.entity.BlockEntity;
import br.com.kelvin.persistence.entity.BoardColumnEntity;
import br.com.kelvin.persistence.entity.BoardEntity;
import br.com.kelvin.persistence.entity.CardEntity;
import br.com.kelvin.persistence.migrations.MigrationStrategy;
import br.com.kelvin.service.BoardService;
import br.com.kelvin.service.CardService;
import br.com.kelvin.ui.InitialMenu;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;

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

