package br.com.kelvin.persistence.migrations;

import br.com.kelvin.persistence.config.ConnectionFactory;
import org.flywaydb.core.Flyway;

public class MigrationStrategy {

    public void executeMigration() {
        Flyway flyway = ConnectionFactory.getFlyway();
        flyway.repair();
        flyway.migrate();
        System.out.println("Migrations aplicadas com sucesso!");
    }
}
