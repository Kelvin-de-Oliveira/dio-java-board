package br.com.kelvin.persistence.dao;

import br.com.kelvin.persistence.entity.BlockEntity;
import br.com.kelvin.persistence.entity.BoardColumnEntity;
import br.com.kelvin.persistence.entity.CardEntity;
import lombok.AllArgsConstructor;

import java.sql.*;

@AllArgsConstructor
public class CardDAO {

    private final Connection connection;

    public void insert(CardEntity card) throws SQLException {
        String sql = "INSERT INTO CARDS (colum_id, title, description) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, card.getColumn().getId());  // referência à coluna
            stmt.setString(2, card.getTitle());
            stmt.setString(3, card.getDescription());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    card.setId(rs.getLong(1));
                }
            }
        }
    }

    public void moveToColumn(CardEntity card, Long newColumnId) throws SQLException {
        String sql = "UPDATE CARDS SET colum_id = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, newColumnId);
            stmt.setLong(2, card.getId());
            int updatedRows = stmt.executeUpdate();

            if (updatedRows > 0) {
                // Busca a nova coluna do banco e atualiza o objeto
                BoardColumnDAO columnDAO = new BoardColumnDAO(connection);
                BoardColumnEntity newColumn = columnDAO.findById(newColumnId);
                card.setColumn(newColumn);
            } else {
                throw new SQLException("Nenhum card encontrado com ID: " + card.getId());
            }
        }
    }

    public boolean isCardBlocked(CardEntity card) throws SQLException {
        String sql = "SELECT COUNT(*) FROM BLOCKS WHERE card_id = ? AND unblocked_at IS NULL";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, card.getId());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    public BlockEntity getActiveBlock(CardEntity card) throws SQLException {
        String sql = "SELECT id, block_reason, blocked_at, unblocked_reason, unblocked_at " +
                "FROM BLOCKS " +
                "WHERE card_id = ? AND unblocked_at IS NULL " +
                "ORDER BY blocked_at DESC LIMIT 1";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, card.getId());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    BlockEntity block = new BlockEntity();
                    block.setId(rs.getLong("id"));
                    block.setBlockReason(rs.getString("block_reason"));
                    block.setBlockedAt(rs.getTimestamp("blocked_at"));
                    block.setUnblockedReason(rs.getString("unblocked_reason"));
                    block.setUnblockedAt(rs.getTimestamp("unblocked_at"));
                    return block;
                } else {
                    throw new SQLException("Nenhum block ativo encontrado para o card com ID: " + card.getId());
                }
            }
        }
    }

    public Connection getConnection() {
        return connection;
    }

}
