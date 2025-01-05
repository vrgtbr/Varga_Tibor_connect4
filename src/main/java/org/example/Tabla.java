package org.example;

import java.util.Arrays;
import java.io.*;

public class Tabla {
    // A tábla mérete: 6 sor és 7 oszlop .
    private static final int SOROK = 6;
    private static final int OSZLOPOK = 7;

    // A tábla belső reprezentációja: kétdimenziós karaktertömb.
    // Az üres helyek ' ' karakterrel vannak inicializálva.
    private char[][] board;

    // Konstruktor: Létrehoz egy üres táblát, amely minden mezőjén ' ' karaktert tartalmaz.
    public Tabla() {
        this.board = new char[SOROK][OSZLOPOK];
        for (int i = 0; i < SOROK; i++) {
            Arrays.fill(board[i], ' '); // Az adott sor minden mezőjét üresre állítja.
        }
    }

    /**
     * Betölti a tábla állapotát egy fájlból.

     */
    public void loadFromFile(String filename) {
        File file = new File(filename);
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
                for (int i = 0; i < SOROK; i++) {
                    String line = reader.readLine(); // sor beolvasása.
                    for (int j = 0; j < OSZLOPOK && j < line.length(); j++) {
                        board[i][j] = line.charAt(j); // Sor karaktereit másolja a táblába.
                    }
                }
                System.out.println("Betöltöttük az állást a(z) " + filename + " fájlból.");
            } catch (IOException e) { // Hiba esetén üzenet jelenik meg.
                System.out.println("Hiba történt a betöltés során: " + e.getMessage());
            }
        } else {
            System.out.println("Sajnos nem  található mentett állás. Üres táblával indul a játék.");
        }
    }

    /**
     * Menteni a tábla aktuális állapotát egy fájlba.
     * @param filename A fájl neve, ahová az állapotot menteni kívánjuk.
     */
    public void saveToFile(String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (int i = 0; i < SOROK; i++) {
                for (int j = 0; j < OSZLOPOK; j++) {
                    writer.write(board[i][j]); // Sor karaktereit kiírja a fájlba.
                }
                writer.newLine(); // Új sort kezd a fájlban.
            }
            System.out.println("Játékállás mentve a(z) " + filename + " fájlba.");
        } catch (IOException e) { // Hiba esetén üzenet jelenik meg.
            System.out.println("Hiba történt a mentés során: " + e.getMessage());
        }
    }

    /**
     * Ellenőrzi, hogy van-e mentett játék a megadott fájlban.

     */
    public boolean hasSavedGame(String filename) {
        return new File(filename).exists();
    }

    /**
     * Reseteli a táblát üres állapotba
     */
    public void resetBoard() {
        for (int i = 0; i < SOROK; i++) {
            Arrays.fill(board[i], ' '); // Minden mező reset.
        }
    }

    /**
     * Kiírja a táblát a konzolra színezett karakterekkel.
     * Az 'X' sárga, az 'O' piros, az üres helyek alapértelmezett színnel jelennek meg.
     */
    public void printBoard() {
        System.out.println("  A B C D E F G"); // Oszlopok betűjelzése.
        for (int i = 0; i < SOROK; i++) {
            System.out.print((SOROK - i) + " "); // Sorok számozása (felülről lefelé).
            for (int j = 0; j < OSZLOPOK; j++) {
                // A mező színe és értéke alapján választjuk ki a színt.
                if (board[i][j] == 'X') {
                    System.out.print("\u001B[33m" + board[i][j] + "\u001B[0m "); // Sárga.
                } else if (board[i][j] == 'O') {
                    System.out.print("\u001B[31m" + board[i][j] + "\u001B[0m "); // Piros.
                } else {
                    System.out.print("\u001B[0m- "); // Üres hely '-'.
                }
            }
            System.out.println();
        }
    }

    /**
     * Korong elhelyezése az adott oszlopba.
     * @param column Az oszlop száma, ahová a korongot helyezzük.
     * @param symbol A játékos szimbóluma ('X' vagy 'O').
     * @return Igaz, ha a korongot sikerült elhelyezni, különben hamis.
     */
    public boolean dropDisk(int column, char symbol) {
        for (int row = 5; row >= 0; row--) { // Sorokat alulról felfelé járjuk be.
            if (board[row][column] == ' ') { // Az első üres helyre helyezi a korongot.
                board[row][column] = symbol;
                return true; // Sikeres elhelyezés.
            }
        }
        return false; // Ha az oszlop tele van, nem lehet korongot helyezni.
    }

    /**
     * Ellenőrzi, hogy az adott oszlopba lehet-e korongot helyezni.

     */
    public boolean isColumnAvailable(int column) {
        return board[0][column] == ' '; // Ha az első sorban van hely, az oszlop elérhető.
    }

    /**
     * Visszaadja a tábla belső állapotát kétdimenziós tömbként.
     * @return A tábla jelenlegi állapota.
     */
    public char[][] getBoard() {
        return board;
    }
}