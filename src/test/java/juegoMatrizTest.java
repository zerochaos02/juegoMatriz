import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

class juegoMatrizTest {

    private String[][] mapa;
    private int[] jugador;
    private int[] enemigo;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outContent));
        mapa = new String[10][10];
        jugador = new int[]{1, 1, 100, 15};
        enemigo = new int[]{5, 5, 45, 10};
        juegoMatriz.llenarMapa(mapa, jugador, enemigo);
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    void revisarEstadoJuego_PlayerNotFound() {
        jugador[0] = 1; // Establecer la posición x del jugador
        jugador[1] = 1; // Establecer la posición y del jugador
        mapa[1][1] = "."; // Eliminar la posición del jugador
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            juegoMatriz.revisarEstadoJuego(jugador, mapa);
        });
        assertEquals("No se encontró la posición del jugador en el mapa", exception.getMessage());
    }

    @Test
    void revisarEstadoJuego_PlayerDies() {
        jugador[2] = 0;
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            juegoMatriz.revisarEstadoJuego(jugador, mapa);
        });
        assertEquals("El jugador ha muerto", exception.getMessage());
    }

    @Test
    void revisarPosicionActual_FindsPlayerPosition() {
        int[] posicion = juegoMatriz.revisarPosicionActual(mapa);
        assertArrayEquals(new int[]{1, 1}, posicion);
    }

    @Test
    void moverJugador_ValidMove() {
        juegoMatriz.moverJugador(mapa, "s", jugador);
        assertEquals("J", mapa[2][1]);
        assertEquals(".", mapa[1][1]);
    }

    @Test
    void moverJugador_InvalidMoveBlockedByWall() {
        mapa[2][1] = "#";
        juegoMatriz.moverJugador(mapa, "s", jugador);
        assertEquals("J", mapa[1][1]);
        assertEquals("#", mapa[2][1]);
    }

    @Test
    void moverJugador_OutOfBounds() {
        jugador[0] = 0; // Establecer la posición x del jugador
        jugador[1] = 0; // Establecer la posición y del jugador
        mapa[0][0] = "J"; // Colocar al jugador en la posición inicial
        juegoMatriz.moverJugador(mapa, "w", jugador); // Intentar mover fuera del mapa
        assertEquals("J", mapa[0][0]); // La posición del jugador no debe cambiar
    }
    @Test
    void eventoCofre_OpenChestReward() {
        jugador[2] = 70;
        mapa[1][2] = "C";
        String input = "si\n";
        System.setIn(new java.io.ByteArrayInputStream(input.getBytes()));
        juegoMatriz.moverJugador(mapa, "d", jugador);
        assertTrue(jugador[2] == 100 || jugador[2] == 40);
    }

    @Test
    void eventoCofre_OpenChestTrap() {
        jugador[2] = 40;
        mapa[1][2] = "C";
        String input = "si\n";
        System.setIn(new java.io.ByteArrayInputStream(input.getBytes()));
        juegoMatriz.moverJugador(mapa, "d", jugador);
        assertTrue(jugador[2] == 70 || jugador[2] == 10);
    }

    @Test
    void eventoCofre_DoNotOpenChest() {
        jugador[2] = 70;
        mapa[1][2] = "C";
        String input = "no\n";
        System.setIn(new java.io.ByteArrayInputStream(input.getBytes()));
        juegoMatriz.moverJugador(mapa, "d", jugador);
        assertEquals(70, jugador[2]);
    }

    @Test
    void combateEnemigo_PlayerWins() {
        int[] enemigo = {5, 5, 10, 5};
        boolean result = juegoMatriz.combateEnemigo(jugador, enemigo, mapa);
        assertFalse(result);
        assertTrue(jugador[2] > 0);
    }

}