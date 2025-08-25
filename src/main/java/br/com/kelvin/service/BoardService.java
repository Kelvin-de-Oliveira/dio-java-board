package br.com.kelvin.service;

import br.com.kelvin.persistence.dao.BoardColumnDAO;
import br.com.kelvin.persistence.dao.BoardDAO;
import br.com.kelvin.persistence.entity.BoardColumnEntity;
import br.com.kelvin.persistence.entity.BoardEntity;
import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@AllArgsConstructor
public class BoardService {


    private final Connection connection;

    public void createBoard(BoardEntity board) {
        BoardDAO boardDAO = new BoardDAO(connection);
        BoardColumnDAO columnDAO = new BoardColumnDAO(connection);

        try {
            validateBoard(board);

            boardDAO.insert(board);

            List<BoardColumnEntity> columns = board.getColumns();
            for (BoardColumnEntity column : columns) {
                column.setBoard(board);
                columnDAO.insert(column);
            }

            connection.commit();
            System.out.println("Board e colunas inseridos com sucesso! ID do board: " + board.getId());

        } catch (SQLException | IllegalArgumentException e) {
            try { connection.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            System.err.println("Erro ao inserir board: " + e.getMessage());
        }
    }


    public void deleteBoard(Long id) {
        BoardDAO boardDAO = new BoardDAO(connection);
        try {
            boardDAO.delete(id);
            connection.commit();
            System.out.println("Board com ID " + id + " deletado com sucesso!");
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            System.err.println("Erro ao deletar o Board com ID " + id + ": " + e.getMessage());
        }
    }

    private void validateBoard(BoardEntity board) {
        List<BoardColumnEntity> columns = board.getColumns();

        if (columns.size() < 3) {
            throw new IllegalArgumentException("Um board deve ter pelo menos 3 colunas.");
        }

        int initialCount = 0;
        int finalCount = 0;
        int cancelledCount = 0;

        for (BoardColumnEntity col : columns) {
            switch (col.getColType()) {
                case INITIAL -> initialCount++;
                case FINAL -> finalCount++;
                case CANCELLED -> cancelledCount++;
                case PENDING -> {} // sem limite
            }
        }

        if (initialCount != 1 || finalCount != 1 || cancelledCount != 1) {
            throw new IllegalArgumentException("O board deve ter 1 coluna INITIAL, 1 FINAL e 1 CANCELLED.");
        }

        if (columns.get(0).getColType() != BoardColumnEntity.ColumnType.INITIAL ||
                columns.get(columns.size() - 2).getColType() != BoardColumnEntity.ColumnType.FINAL ||
                columns.get(columns.size() - 1).getColType() != BoardColumnEntity.ColumnType.CANCELLED) {
            throw new IllegalArgumentException("As colunas devem seguir a ordem: INITIAL, PENDING (0+), FINAL, CANCELLED.");
        }
    }

}
