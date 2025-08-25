package br.com.kelvin.persistence.dao;

import br.com.kelvin.persistence.entity.BoardEntity;
import lombok.AllArgsConstructor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class BoardDAO {

    private final Connection connection;

    public void insert(BoardEntity board) throws SQLException {
        String sql = "INSERT INTO BOARDS (name) VALUES (?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, board.getName());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    board.setId(rs.getLong(1));
                }
            }
        }
    }

    public void delete(Long id) throws SQLException {
        String sql = "DELETE FROM BOARDS WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }

    public Optional<BoardEntity> findById(Long id) throws SQLException {
        String sql = "SELECT id, name FROM BOARDS WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    BoardEntity board = new BoardEntity();
                    board.setId(rs.getLong("id"));
                    board.setName(rs.getString("name"));
                    return Optional.of(board);
                }
            }
        }
        return Optional.empty();
    }

    public List<BoardEntity> findAll() throws SQLException {
        String sql = "SELECT id, name FROM BOARDS";
        List<BoardEntity> boards = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                BoardEntity board = new BoardEntity();
                board.setId(rs.getLong("id"));
                board.setName(rs.getString("name"));
                boards.add(board);
            }
        }

        return boards;
    }
}
