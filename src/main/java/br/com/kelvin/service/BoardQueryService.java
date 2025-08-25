package br.com.kelvin.service;

import br.com.kelvin.persistence.dao.BoardDAO;
import br.com.kelvin.persistence.entity.BoardEntity;
import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class BoardQueryService {

    private final Connection connection;

    public Optional<BoardEntity> findById(Long id) {
        BoardDAO boardDAO = new BoardDAO(connection);
        try {
            return boardDAO.findById(id);
        } catch (SQLException e) {
            System.err.println("Erro ao buscar Board com ID " + id + ": " + e.getMessage());
            return Optional.empty();
        }
    }

    public List<BoardEntity> findAllBoards() {
        BoardDAO boardDAO = new BoardDAO(connection);
        try {
            return boardDAO.findAll();
        } catch (SQLException e) {
            System.err.println("Erro ao buscar todos os boards: " + e.getMessage());
            return List.of();
        }
    }

}