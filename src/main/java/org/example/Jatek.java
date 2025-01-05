package org.example;
import java.util.Random;
import java.util.Scanner;

import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;



public class Jatek {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(Jatek.class);
    private final Tabla board; // A játéktábla
    private final Jatekos player1; // Első játékos (ember)
    private final Jatekos player2; // Második játékos (gép)
    private Jatekos currentJatekos; // Aktuális játékos
    private boolean isComputerTurn; // Jelezze, hogy most a gép jön-e
    private final Random random; // Random generátor a gép számára
    private static final String SAVE_FILE = "connect4_save.txt"; // Mentési fájl neve


    public Jatek(Jatekos player1, Jatekos player2) {
        this.board = new Tabla(); // Új játéktábla létrehozása
        this.player1 = player1; // Az emberi játékos hozzárendelése
        this.player2 = player2; // A gépi játékos hozzárendelése
        this.currentJatekos = player1; // Az emberi játékos kezd
        this.isComputerTurn = false; // Az első körben az ember játszik
        this.random = new Random(); // Random szám generátor inicializálása
    }

    /**
     * ember és játékos közötti váltás
     */
    public void switchPlayer() {
        this.currentJatekos = (this.currentJatekos == player1) ? player2 : player1;
        this.isComputerTurn = !this.isComputerTurn; // Gép és ember közti váltás
    }

    /**
     * Korong elhelyezése a megadott oszlopban.
     * @param column Az oszlop indexe (0-6).
     * @return True, ha sikeres a lépés, különben false.
     */
    public boolean dropDisk(int column) {
        return board.dropDisk(column, currentJatekos.symbol());
    }

    /**
     * A gépi játékos véletlenszerű lépése.
     * @return Az oszlop indexe (0-6), ahova a gép lépni fog.
     */
    public int computerMove() {
        int column;
        do {
            column = random.nextInt(7); // Véletlenszerű oszlop választása
        } while (!board.isColumnAvailable(column)); // Addig próbálkozunk, amíg találunk üres oszlopot
        return column;
    }

    /**
     * Ellenőrzi, hogy a legutóbbi lépés győzelmet eredményezett-e.

     */
    public boolean checkWin(int row, int col) {
        char symbol = board.getBoard()[row][col]; // Az aktuális játékos szimbóluma
        return checkDirection(row, col, 1, 0, symbol) || // Vízszintes ELLENŐRZÉS STB..
                checkDirection(row, col, 0, 1, symbol) ||
                checkDirection(row, col, 1, 1, symbol) ||
                checkDirection(row, col, 1, -1, symbol);
    }

    /**
     * Ellenőrzi, hogy az adott irányban (dx, dy) van-e 4 egymás melletti korong.

     */
    private boolean checkDirection(int row, int col, int dx, int dy, char symbol) {
        int count = 1;

        // Előre haladás az adott irányban
        int r = row + dx;
        int c = col + dy;
        while (r >= 0 && r < 6 && c >= 0 && c < 7 && board.getBoard()[r][c] == symbol) {
            count++;
            r += dx;
            c += dy;
        }

        // Visszafelé haladás az adott irányban
        r = row - dx;
        c = col - dy;
        while (r >= 0 && r < 6 && c >= 0 && c < 7 && board.getBoard()[r][c] == symbol) {
            count++;
            r -= dx;
            c -= dy;
        }

        return count >= 4; // Ha 4 vagy több azonos szimbólum van egy sorban, nyer
    }

    /**
     * A játék fő folyamata.
     */
    public void startGame() {
        Scanner scanner = new Scanner(System.in);
        Database database = new Database();
        database.addPlayer(player1.name());
        database.addPlayer(player2.name());

        // Mentett játék betöltése
        if (board.hasSavedGame(SAVE_FILE)) {
            System.out.println("Mentett játékállás található. Be akarod tölteni? (i/n)");
            String input = scanner.nextLine().toLowerCase();
            if (input.equals("i")) {
                board.loadFromFile(SAVE_FILE); // Betöltés
            } else {
                System.out.println("Üres táblával indul a játék.");
                board.resetBoard();
            }
        } else {
            System.out.println("Nem található mentett állás. Üres táblával indul a játék.");
            board.resetBoard();
        }

        boolean gameWon = false;

        // Játékmenet ciklusa
        while (!gameWon) {
            board.printBoard();
            int column = -1;

            // Gép lépése
            if (isComputerTurn) {
                column = computerMove();
                System.out.println("A gép választotta a " + (char) ('a' + column) + " oszlopot.");
            } else {
                boolean validInput = false;
                while (!validInput) {
                    System.out.println(currentJatekos.name() + ", válassz egy oszlopot (A-G vagy 'mentés'): ");
                    String input = scanner.nextLine().toLowerCase();

                    // Mentés opció
                    if (input.equals("mentés")) {
                        board.saveToFile(SAVE_FILE); // Mentés a fájlba
                        System.out.println("Játékállás mentve a következő fájlba: " + SAVE_FILE);

                        logger.info ("Nem sikerült menteni a játékállást a fájl ") ;
                        continue;

                    }

                    // Az érvényes oszlop választása
                    if (input.length() == 1 && input.charAt(0) >= 'a' && input.charAt(0) <= 'g') {
                        column = input.charAt(0) - 'a';
                        if (board.isColumnAvailable(column)) {
                            validInput = true; // érvényes választáős
                        } else {
                            System.out.println("Ez az oszlop sajnos már tele van. Próbálj egy másikat.");
                        }
                    } else {
                        System.out.println("Érvénytelen választás. Kérlek válassz egy másik  oszlopot A-G között.");
                    }
                }
            }




            // Korong elhelyez. és győzelem ell.
            if (dropDisk(column)) {
                int row = 0;
                while (row < 6 && board.getBoard()[row][column] != currentJatekos.symbol()) {
                    row++;
                }

                if (checkWin(row, column)) {
                    board.printBoard();
                    System.out.println(currentJatekos.name() + " Nyert");
                    database.updateWins(currentJatekos.name());
                    gameWon = true;
                }

                if (!gameWon) {
                    switchPlayer();
                }
            }
        }
    }

    public Jatekos getCurrentPlayer() {
        return currentJatekos;
    }
}