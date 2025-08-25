package br.com.kelvin.service;

import br.com.kelvin.persistence.dao.BoardColumnDAO;
import br.com.kelvin.persistence.entity.BoardColumnEntity;
import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class BoardColumnQueryService {

    private final Connection connection;

    /*public Optional<BoardColumnEntity> findById(Long id) {
        BoardColumnDAO columnDAO = new BoardColumnDAO(connection);
        try {
            return columnDAO.findById(id); // supondo que BoardColumnDAO já tenha um método findById retornando Optional<BoardColumnEntity>
        } catch (SQLException e) {
            System.err.println("Erro ao buscar BoardColumn com ID " + id + ": " + e.getMessage());
            return Optional.empty();
        }
    }*/
}
