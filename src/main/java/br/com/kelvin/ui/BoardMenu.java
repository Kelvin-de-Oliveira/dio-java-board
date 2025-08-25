package br.com.kelvin.ui;

import br.com.kelvin.persistence.dao.CardDAO;
import br.com.kelvin.persistence.entity.BoardColumnEntity;
import br.com.kelvin.persistence.entity.CardEntity;
import br.com.kelvin.persistence.entity.BoardEntity;
import br.com.kelvin.service.BoardColumnQueryService;
import br.com.kelvin.service.CardService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class BoardMenu {

    private final BoardEntity board;
    private final BoardColumnQueryService columnQueryService;
    private final Connection connection;
    private final Scanner scanner = new Scanner(System.in);

    private static final int COLUMN_WIDTH = 20; // largura fixa para colunas

    public BoardMenu(BoardEntity board, Connection connection) {
        this.board = board;
        this.connection = connection;
        this.columnQueryService = new BoardColumnQueryService(connection);
    }

    public void showBoardMenu() {
        while (true) {
            System.out.println("\n=== Board: " + board.getName() + " ===");

            List<BoardColumnEntity> columns = columnQueryService.findColumnsWithCardsByBoardId(board.getId());
            CardDAO cardDAO = new CardDAO(connection);

            printSeparator(columns.size());
            for (BoardColumnEntity column : columns) {
                System.out.printf("| %-"+COLUMN_WIDTH+"s", column.getName());
            }
            System.out.println("|");
            printSeparator(columns.size());


            int maxCards = columns.stream()
                    .mapToInt(c -> c.getCards().size())
                    .max()
                    .orElse(0);


            for (int i = 0; i < maxCards; i++) {
                for (BoardColumnEntity column : columns) {
                    String cellContent = "";
                    if (i < column.getCards().size()) {
                        CardEntity card = column.getCards().get(i);
                        try {
                            boolean blocked = cardDAO.isCardBlocked(card);
                            cellContent = card.getTitle() + (blocked ? " [BLOQ]" : "");
                        } catch (SQLException e) {
                            cellContent = card.getTitle() + " [ERRO]";
                        }
                    }
                    System.out.printf("| %-"+COLUMN_WIDTH+"s", cellContent);
                }
                System.out.println("|");
            }
            printSeparator(columns.size());

            if (!showOptionsMenu(columns)) {
                return;
            }
        }
    }

    private boolean showOptionsMenu(List<BoardColumnEntity> columns) {
        System.out.println("\nOpções:");
        System.out.println("1 - Criar novo card");
        System.out.println("2 - Selecionar card");
        System.out.println("3 - Fechar board (retornar ao menu inicial)");
        System.out.print("Escolha uma opção: ");

        String input = scanner.nextLine();
        switch (input) {
            case "1" -> createCard(columns);
            case "2" -> selectCard(columns);
            case "3" -> {
                System.out.println("Fechando board...");
                return false;
            }
            default -> System.out.println("Opção inválida, tente novamente.");
        }
        return true;
    }
    private void createCard(List<BoardColumnEntity> columns) {
        CardService cardService = new CardService(connection);

        System.out.print("Digite o título do card: ");
        String title = scanner.nextLine();

        System.out.print("Digite a descrição do card: ");
        String description = scanner.nextLine();


        BoardColumnEntity firstColumn = columns.get(0);

        CardEntity newCard = new CardEntity();
        newCard.setTitle(title);
        newCard.setDescription(description);
        newCard.setColumn(firstColumn);

        cardService.createCard(newCard);

        System.out.println("Novo card criado na coluna: " + firstColumn.getName());
    }

    private void selectCard(List<BoardColumnEntity> columns) {
        System.out.print("Digite o título do card que deseja selecionar: ");
        String title = scanner.nextLine();

        CardEntity selectedCard = null;
        for (BoardColumnEntity column : columns) {
            for (CardEntity card : column.getCards()) {
                if (card.getTitle().equalsIgnoreCase(title)) {
                    selectedCard = card;
                    break;
                }
            }
            if (selectedCard != null) break;
        }

        if (selectedCard == null) {
            System.out.println("Card não encontrado!");
        } else {
            CardMenu cardMenu = new CardMenu(selectedCard, connection);
            cardMenu.showCardMenu();
        }
    }
    private void printSeparator(int columnCount) {
        for (int i = 0; i < columnCount; i++) {
            System.out.print("+" + "-".repeat(COLUMN_WIDTH));
        }
        System.out.println("+");
    }

}
