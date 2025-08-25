package br.com.kelvin.ui;

import br.com.kelvin.persistence.entity.CardEntity;
import br.com.kelvin.service.CardService;

import java.sql.Connection;
import java.util.Scanner;

public class CardMenu {

    private final CardEntity card;
    private final CardService cardService;
    private final Scanner scanner = new Scanner(System.in);

    public CardMenu(CardEntity card, Connection connection) {
        this.card = card;
        this.cardService = new CardService(connection);
    }

    public void showCardMenu() {
        while (true) {
            System.out.println("\n=== Card: " + card.getTitle() + " ===");
            System.out.println("Descrição: " + card.getDescription());
            System.out.println("Coluna atual: " + card.getColumn().getName());

            System.out.println("\nOpções:");
            System.out.println("1 - Mover para próxima coluna");
            System.out.println("2 - Cancelar card");
            System.out.println("3 - Bloquear card");
            System.out.println("4 - Desbloquear card");
            System.out.println("5 - Voltar para o board");
            System.out.print("Escolha uma opção: ");

            String input = scanner.nextLine();
            switch (input) {
                case "1" -> System.out.println("verfiicar oq há de errado"); // cardService.moveToNextColumn(card);
                case "2" ->    System.out.println("verfiicar oq há de errado"); // cardService.cancelCard(card);
                case "3" -> {
                    System.out.print("Informe o motivo do bloqueio: ");
                    String reason = scanner.nextLine();
                    cardService.blockCard(card, reason);
                }
                case "4" -> {
                    System.out.print("Informe o motivo do desbloqueio: ");
                    String reason = scanner.nextLine();
                    cardService.unblockCard(card, reason);
                }
                case "5" -> {
                    System.out.println("Voltando para o board...");
                    return; // encerra o menu do card
                }
                default -> System.out.println("Opção inválida, tente novamente.");
            }
        }
    }
}
