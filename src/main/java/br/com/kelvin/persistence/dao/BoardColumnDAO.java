package br.com.kelvin.persistence.dao;

import br.com.kelvin.persistence.entity.BoardColumnEntity;
import br.com.kelvin.persistence.entity.BoardEntity;
import br.com.kelvin.persistence.entity.CardEntity;
import lombok.AllArgsConstructor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class BoardColumnDAO {

    private final Connection connection;

    public void insert(BoardColumnEntity column) throws SQLException {
        String sql = "INSERT INTO BOARD_COLUMNS (board_id, name, col_order, col_type) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, column.getBoard().getId());
            stmt.setString(2, column.getName());
            stmt.setInt(3, column.getColOrder());
            stmt.setString(4, column.getColType().name());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    column.setId(rs.getLong(1));
                }
            }
        }
    }
    public BoardColumnEntity findById(Long columnId) throws SQLException {
        String sql = "SELECT id, board_id, name, col_order, col_type FROM BOARD_COLUMNS WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, columnId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    BoardColumnEntity column = new BoardColumnEntity();
                    column.setId(rs.getLong("id"));
                    column.setName(rs.getString("name"));
                    column.setColOrder(rs.getInt("col_order"));
                    column.setColType(BoardColumnEntity.ColumnType.valueOf(rs.getString("col_type")));

                    // Cria referência mínima do Board
                    BoardEntity board = new BoardEntity();
                    board.setId(rs.getLong("board_id"));
                    column.setBoard(board);

                    return column;
                } else {
                    throw new SQLException("Coluna não encontrada com ID: " + columnId);
                }
            }
        }
    }

    public List<BoardColumnEntity> findByBoardId(Long boardId) throws SQLException {
        String sql = "SELECT id, name, col_order, col_type FROM BOARD_COLUMNS WHERE board_id = ? ORDER BY col_order";
        List<BoardColumnEntity> columns = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, boardId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    BoardColumnEntity column = new BoardColumnEntity();
                    column.setId(rs.getLong("id"));
                    column.setName(rs.getString("name"));
                    column.setColOrder(rs.getInt("col_order"));
                    column.setColType(BoardColumnEntity.ColumnType.valueOf(rs.getString("col_type")));
                    
                    BoardEntity board = new BoardEntity();
                    board.setId(boardId);
                    column.setBoard(board);

                    columns.add(column);
                }
            }
        }
        return columns;
    }
    public List<BoardColumnEntity> findColumnsWithCardsByBoardId(Long boardId) throws SQLException {
        String sql = """
            SELECT c.id AS col_id, c.name AS col_name, c.col_order, c.col_type,
                   ca.id AS card_id, ca.title, ca.description
            FROM BOARD_COLUMNS c
            LEFT JOIN CARDS ca ON c.id = ca.colum_id
            WHERE c.board_id = ?
            ORDER BY c.col_order, ca.id
        """;

        List<BoardColumnEntity> columns = new ArrayList<>();
        BoardColumnEntity currentColumn = null;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, boardId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    long colId = rs.getLong("col_id");

                    if (currentColumn == null || currentColumn.getId() != colId) {
                        currentColumn = new BoardColumnEntity();
                        currentColumn.setId(colId);
                        currentColumn.setName(rs.getString("col_name"));
                        currentColumn.setColOrder(rs.getInt("col_order"));
                        currentColumn.setColType(BoardColumnEntity.ColumnType.valueOf(rs.getString("col_type")));
                        columns.add(currentColumn);
                    }

                    long cardId = rs.getLong("card_id");
                    if (!rs.wasNull()) {
                        CardEntity card = new CardEntity();
                        card.setId(cardId);
                        card.setTitle(rs.getString("title"));
                        card.setDescription(rs.getString("description"));
                        card.setColumn(currentColumn);
                        currentColumn.getCards().add(card);
                    }
                }
            }
        }

        return columns;
    }


}
