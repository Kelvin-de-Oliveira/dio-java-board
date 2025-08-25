package br.com.kelvin.service;

import br.com.kelvin.persistence.dao.BlockDAO;
import br.com.kelvin.persistence.dao.CardDAO;
import br.com.kelvin.persistence.entity.BlockEntity;
import br.com.kelvin.persistence.entity.BoardColumnEntity;
import br.com.kelvin.persistence.entity.CardEntity;
import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@AllArgsConstructor
public class CardService {

    private final Connection connection;

    public void createCard(CardEntity card) {
        CardDAO cardDAO = new CardDAO(connection);

        try {
            cardDAO.insert(card);

            connection.commit();
            System.out.println("Card inserido com sucesso! ID do card: " + card.getId());

        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            System.err.println("Erro ao inserir card: " + e.getMessage());
        }
    }

    public void moveToNextColumn(CardEntity card) {
        CardDAO cardDAO = new CardDAO(connection);

        try {
            if (cardDAO.isCardBlocked(card)) {
                System.err.println("O card está bloqueado e não pode ser movido.");
                return;
            }

            BoardColumnEntity nextColumn = getBoardColumnEntity(card);
            cardDAO.moveToColumn(card, nextColumn.getId());
            connection.commit();
            System.out.println("Card movido para a coluna: " + nextColumn.getName());

        } catch (SQLException | IllegalStateException e) {
            try { connection.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            System.err.println("Erro ao mover card: " + e.getMessage());
        }
    }
    public void cancelCard(CardEntity card) {
        CardDAO cardDAO = new CardDAO(connection);

        try {
            if (cardDAO.isCardBlocked(card)) {
                System.err.println("O card está bloqueado e não pode ser cancelado.");
                return;
            }

            List<BoardColumnEntity> columns = card.getColumn().getBoard().getColumns();
            BoardColumnEntity cancelColumn = columns.get(columns.size() - 1);

            cardDAO.moveToColumn(card, cancelColumn.getId());
            connection.commit();
            System.out.println("Card cancelado e movido para a coluna: " + cancelColumn.getName());

        } catch (SQLException e) {
            try { connection.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            System.err.println("Erro ao cancelar card: " + e.getMessage());
        }
    }

    private static BoardColumnEntity getBoardColumnEntity(CardEntity card) {
        BoardColumnEntity currentColumn = card.getColumn();
        List<BoardColumnEntity> columns = currentColumn.getBoard().getColumns();

        int currentIndex = -1;
        for (int i = 0; i < columns.size(); i++) {
            if (columns.get(i).getId().equals(currentColumn.getId())) {
                currentIndex = i;
                break;
            }
        }

        if (currentIndex == -1 || currentIndex == columns.size() - 1) {
            throw new IllegalStateException("Não é possível mover o card: coluna atual inválida ou última coluna.");
        }

        return columns.get(currentIndex + 1);
    }

    public void blockCard(CardEntity card, String reason) {
        CardDAO cardDAO = new CardDAO(connection);
        BlockDAO blockDAO = new BlockDAO(connection);

        try {
            if (cardDAO.isCardBlocked(card)) {
                System.err.println("O card já está bloqueado.");
                return;
            }

            BlockEntity block = new BlockEntity();
            block.setBlockReason(reason);
            block.setBlockedAt(new Timestamp(System.currentTimeMillis()));

            blockDAO.block(block, card);
            connection.commit();
            System.out.println("Card bloqueado com sucesso! Motivo: " + reason);

        } catch (SQLException e) {
            try { connection.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            System.err.println("Erro ao bloquear card: " + e.getMessage());
        }
    }

    public void unblockCard(CardEntity card, String reason) {
        CardDAO cardDAO = new CardDAO(connection);
        BlockDAO blockDAO = new BlockDAO(connection);

        try {
            if (!cardDAO.isCardBlocked(card)) {
                System.err.println("O card não está bloqueado.");
                return;
            }

            // Recupera o último block ativo do card
            BlockEntity block = cardDAO.getActiveBlock(card);
            blockDAO.unblock(block, reason);
            connection.commit();
            System.out.println("Card desbloqueado com sucesso! Motivo: " + reason);

        } catch (SQLException e) {
            try { connection.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            System.err.println("Erro ao desbloquear card: " + e.getMessage());
        }
    }

}