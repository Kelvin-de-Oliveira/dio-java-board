package br.com.kelvin.persistence.dao;

import br.com.kelvin.persistence.entity.BlockEntity;
import br.com.kelvin.persistence.entity.CardEntity;
import lombok.AllArgsConstructor;

import java.sql.*;

@AllArgsConstructor
public class BlockDAO {

    private final Connection connection;

    public void block(BlockEntity block, CardEntity card) throws SQLException {
        String sql = "INSERT INTO BLOCKS (card_id, block_reason, blocked_at, unblocked_reason, unblocked_at) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, card.getId());
            stmt.setString(2, block.getBlockReason());
            stmt.setTimestamp(3, block.getBlockedAt() != null ? block.getBlockedAt() : new Timestamp(System.currentTimeMillis()));
            stmt.setString(4, block.getUnblockedReason() != null ? block.getUnblockedReason() : "");
            stmt.setTimestamp(5, block.getUnblockedAt());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    block.setId(rs.getLong(1));
                }
            }
        }
    }

    public void unblock(BlockEntity block, String unblockReason) throws SQLException {
        String sql = "UPDATE BLOCKS SET unblocked_reason = ?, unblocked_at = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, unblockReason);
            stmt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            stmt.setLong(3, block.getId());

            int updatedRows = stmt.executeUpdate();

            if (updatedRows == 0) {
                throw new SQLException("Nenhum block encontrado com ID: " + block.getId());
            } else {
                block.setUnblockedReason(unblockReason);
                block.setUnblockedAt(new Timestamp(System.currentTimeMillis()));
            }
        }
    }
}
