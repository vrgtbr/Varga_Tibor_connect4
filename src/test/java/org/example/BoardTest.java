package org.example;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BoardTest {
    private Tabla tabla;

    @BeforeEach
    void setUp() {
        tabla = new Tabla();
    }

    @Test
    void testDropDisk() {
        Assertions.assertTrue(tabla.dropDisk(0, 'X')); // Üres oszlopba sikeres elhelyezés
        Assertions.assertEquals('X', tabla.getBoard()[5][0]); // Korong az alsó sorban
    }

    @Test
    void testIsColumnAvailable() {
        Assertions.assertTrue(tabla.isColumnAvailable(0)); // Kezdetben elérhető az oszlop
        tabla.dropDisk(0, 'X');
        Assertions.assertTrue(tabla.isColumnAvailable(0)); // Egy koronggal még mindig elérhető
    }

    @Test
    void testResetBoard() {
        tabla.dropDisk(0, 'X');
        tabla.resetBoard();
        for (char[] row : tabla.getBoard()) {
            for (char cell : row) {
                Assertions.assertEquals(' ', cell); // Minden mező üres
            }
        }
    }
}