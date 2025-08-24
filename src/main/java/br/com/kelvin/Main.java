package br.com.kelvin;

import br.com.kelvin.persitence.config.ConectionConfig;

import java.sql.SQLException;

public class Main {

    public static void main(String[] args){
        try(var connection = ConectionConfig.getConnection()){
            System.out.println("Conectou!");
        }
        catch(SQLException e){
            e.printStackTrace();
        }

    }
}
