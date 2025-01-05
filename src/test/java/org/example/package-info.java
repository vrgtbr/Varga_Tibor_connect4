/**

 package org.example;

 import org.junit.jupiter.api.BeforeEach;
 import org.junit.jupiter.api.Test;

 import static org.junit.jupiter.api.Assertions.*;

 class GameTest {
 private Game game;
 private Player player1;
 private Player player2;

 @BeforeEach
 void setUp() {
 player1 = new Player("Player1", 'X', "Yellow");
 player2 = new Player("Player2", 'O', "Red");
 game = new Game(player1, player2);
 }

 @Test
 void testSwitchPlayer() {
 assertEquals(player1, game.getCurrentPlayer());
 game.switchPlayer();
 assertEquals(player2, game.getCurrentPlayer());
 }

 @Test
 void testComputerMove() {
 int column = game.computerMove();
 assertTrue(column >= 0 && column < 7); // Az oszlop érvényes legyen
 }

 @Test
 void testCheckWinHorizontal() {
 // Korongok elhelyezése vízszintes nyerő helyzethez
 for (int i = 0; i < 4; i++) {
 game.dropDisk(i);
 game.switchPlayer(); // Váltás a következő játékosra
 }
 assertTrue(game.checkWin(5, 3)); // Győzelem ellenőrzése
 }
 }
 */
package org.example;