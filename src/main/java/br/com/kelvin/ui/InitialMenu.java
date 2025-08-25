package br.com.kelvin.ui;

import br.com.kelvin.persistence.entity.BoardColumnEntity;
import br.com.kelvin.persistence.entity.BoardEntity;
import br.com.kelvin.service.BoardQueryService;
import br.com.kelvin.service.BoardService;
import lombok.RequiredArgsConstructor;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@RequiredArgsConstructor
public class InitialMenu {

    private final Scanner scanner = new Scanner(System.in);
    private final BoardService boardService;
    private final BoardQueryService boardQueryService;
    private final Connection connection;

    public InitialMenu(Connection connection) {
        this.connection = connection;
        this.boardService = new BoardService(connection);
        this.boardQueryService = new BoardQueryService(connection);
    }

    public void showMenu() {
        int option = 0;
        do {
            System.out.println("\nMenu:");
            System.out.println("1 - Criar novo board");
            System.out.println("2 - Selecionar board");
            System.out.println("3 - Excluir boards");
            System.out.println("4 - Sair");
            System.out.print("Escolha uma opção: ");

            try {
                option = Integer.parseInt(scanner.nextLine());
                switch(option) {
                    case 1 -> createBoard();
                    case 2 -> selectBoard();
                    case 3 -> deleteBoards();
                    case 4 -> System.out.println("Saindo...");
                    default -> System.out.println("Opção inválida, tente novamente.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Digite um número válido.");
            }

        } while(option != 4);
    }

    public void createBoard() {
        System.out.print("Digite o nome do board: ");
        String boardName = scanner.nextLine();

        BoardEntity board = new BoardEntity();
        board.setName(boardName);

        List<BoardColumnEntity> columns = new ArrayList<>(List.of(
                createColumn("Inicial", BoardColumnEntity.ColumnType.INITIAL, 0),
                createColumn("Final", BoardColumnEntity.ColumnType.FINAL, 1),
                createColumn("Cancelado", BoardColumnEntity.ColumnType.CANCELLED, 2)
        ));

        board.setColumns(columns);
        boardService.createBoard(board);
    }

    private BoardColumnEntity createColumn(String name, BoardColumnEntity.ColumnType type, int order) {
        BoardColumnEntity column = new BoardColumnEntity();
        column.setName(name);
        column.setColType(type);
        column.setColOrder(order);
        return column;
    }

    private void selectBoard() {
        List<BoardEntity> boards = boardQueryService.findAllBoards();
        if (boards.isEmpty()) {
            System.out.println("Nenhum board disponível.");
            return;
        }

        System.out.println("Boards disponíveis:");
        for (BoardEntity b : boards) {
            System.out.println(b.getId() + " - " + b.getName());
        }

        System.out.print("Digite o ID do board que deseja selecionar: ");
        try {
            long id = Long.parseLong(scanner.nextLine());
            BoardEntity selectedBoard = boards.stream()
                    .filter(b -> b.getId().equals(id))
                    .findFirst()
                    .orElse(null);

            if (selectedBoard == null) {
                System.out.println("Board com ID " + id + " não encontrado.");
                return;
            }


            BoardMenu boardMenu = new BoardMenu(selectedBoard, connection);
            boardMenu.showBoardMenu();

        } catch (NumberFormatException e) {
            System.out.println("ID inválido.");
        }
    }

    private void deleteBoards() {
        List<BoardEntity> boards = boardQueryService.findAllBoards();
        if (boards.isEmpty()) {
            System.out.println("Nenhum board disponível para exclusão.");
            return;
        }

        System.out.println("Boards disponíveis:");
        for (BoardEntity b : boards) {
            System.out.println(b.getId() + " - " + b.getName());
        }

        System.out.print("Digite o ID do board que deseja deletar: ");
        try {
            long id = Long.parseLong(scanner.nextLine());
            boardService.deleteBoard(id);
        } catch (NumberFormatException e) {
            System.out.println("ID inválido.");
        }
    }
}
