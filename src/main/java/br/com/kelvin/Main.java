package br.com.kelvin;

import br.com.kelvin.persistence.config.ConnectionFactory;
import br.com.kelvin.persistence.migrations.MigrationStrategy;
import org.flywaydb.core.Flyway;

import java.sql.SQLException;

public class Main {

    public static void main(String[] args){
            MigrationStrategy migrationStrategy = new MigrationStrategy();
            migrationStrategy.executeMigration();
    }
}

