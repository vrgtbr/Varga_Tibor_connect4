package org.example;
import java.util.Scanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Connect4 {
    private static final Logger logger = LoggerFactory.getLogger(Connect4.class);
    public static void main(final String[] args) {
        Scanner scanner = new Scanner(System.in);
        logger.info("Connect4 játék indítása...");


        // Emberi játékos nevének bekérése
        System.out.print("Kérlek add meg játékos nevét: ");
        String playerName = scanner.nextLine(); // A név beolvasása

        // Emberi játékos létrehozása.
        Jatekos player1 = new Jatekos(playerName, 'X', "\u001B[33m");

        // Gépi játékos létrehozása.
        Jatekos player2 = new Jatekos("Gép", 'O', "\u001B[31m");

        // Játék inicializálása az emberi és gépi játékossal.
        Jatek jatek = new Jatek(player1, player2);


        jatek.startGame();

        Database database = new Database();

        database.createTable();

        database.addPlayer(playerName);


        System.out.print("Szeretnéd megjeleníteni a highscore táblázatot? (igen/nem): ");
        String showHighScores = scanner.nextLine().toLowerCase();


        if ("igen".equals(showHighScores)) {
            database.printHighScores();
        } else {
            System.out.println("A játék véget ért.");
        }


        scanner.close();
    }
}