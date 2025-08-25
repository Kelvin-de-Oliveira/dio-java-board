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


    public Optional<List<BoardColumnEntity>> findColumnsByBoardId(Long boardId) {
        BoardColumnDAO dao = new BoardColumnDAO(connection);
        try {
            return Optional.ofNullable(dao.findByBoardId(boardId));
        } catch (SQLException e) {
            System.err.println("Erro ao buscar colunas do board: " + e.getMessage());
            return Optional.empty();
        }
    }

    public List<BoardColumnEntity> findColumnsWithCardsByBoardId(Long boardId) {
        BoardColumnDAO dao = new BoardColumnDAO(connection);
        try {
            return dao.findColumnsWithCardsByBoardId(boardId);
        } catch (SQLException e) {
            System.err.println("Erro ao buscar colunas e cards do board: " + e.getMessage());
            return List.of();
        }
    }
}
