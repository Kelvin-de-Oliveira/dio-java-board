package br.com.kelvin.persistence.dao;

import br.com.kelvin.persistence.entity.BoardColumnEntity;
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

    public Optional<List<BoardColumnEntity>> findByBoardId(Long boardId) throws SQLException {
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
                    columns.add(column);
                }
            }
        }

        return columns.isEmpty() ? Optional.empty() : Optional.of(columns);
    }


}
