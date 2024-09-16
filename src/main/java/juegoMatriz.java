import java.util.Scanner;
import java.util.Random;

public class juegoMatriz {

    public static void main(String[] args) {
        try {
            System.out.println("-------------------------------");
            String[][] mapa = new String[10][10];
            int[] jugador = crearJugador(1, 1);
            int[] enemigo = crearEnemigo(5, 5);
            llenarMapa(mapa, jugador, enemigo);
            Scanner scanner = new Scanner(System.in);
            while (true) {
                imprimirMapa(mapa);
                System.out.println("-------------------------------");
                System.out.println("Ingrese un comando de movimiento (w, a, s, d): ");
                String comando = scanner.nextLine();
                moverJugador(mapa, comando, jugador);
                System.out.println("-------------------------------");
            }
        } catch (Exception e) {
            System.out.println("Error en el método main: " + e.getMessage());
        }
    }

    public static int[] crearJugador(int x, int y) {
        try {
            System.out.println("-------------------------------");
            return new int[]{x, y, 100, 15}; // x, y, vida, ataque
        } catch (Exception e) {
            System.out.println("Error al crear jugador: " + e.getMessage());
            return new int[]{0, 0, 0, 0};
        }
    }

    public static int[] crearEnemigo(int x, int y) {
        try {
            System.out.println("-------------------------------");
            return new int[]{x, y, 45, 10}; // x, y, vida, ataque
        } catch (Exception e) {
            System.out.println("Error al crear enemigo: " + e.getMessage());
            return new int[]{0, 0, 0, 0};
        }
    }

    public static void revisarEstadoJuego(int[] jugador, String[][] mapa) {
        if (jugador[2] <= 0) {
            throw new RuntimeException("El jugador ha muerto");
        }
        int[] posicionActual = revisarPosicionActual(mapa);
        if (posicionActual[0] == -1 || posicionActual[1] == -1) {
            throw new RuntimeException("No se encontró la posición del jugador en el mapa");
        }
        if (mapa[posicionActual[0]][posicionActual[1]].equals("X")) {
            throw new RuntimeException("El jugador ha ganado");
        }
    }

    public static int[] revisarPosicionActual(String[][] mapa) {
        try {
            for (int i = 0; i < mapa.length; i++) {
                for (int j = 0; j < mapa[i].length; j++) {
                    if ("J".equals(mapa[i][j])) {
                        return new int[]{i, j};
                    }
                }
            }
            throw new RuntimeException("Player position not found on the map");
        } catch (Exception e) {
            System.out.println("Error al revisar la posición actual: " + e.getMessage());
            return new int[]{-1, -1};
        }
    }

    public static void eventoCofre(int[] jugador, String[][] mapa, int x, int y) {
        try {
            System.out.println("-------------------------------");
            System.out.println("Has encontrado un cofre, ¿lo abres? (si/no): ");
            Scanner scanner = new Scanner(System.in);
            String decision = scanner.nextLine().trim().toLowerCase();

            if (decision.equals("si")) {
                Random random = new Random();
                if (random.nextBoolean()) {
                    jugador[2] += 30;
                    System.out.println("¡Has encontrado una recompensa!");
                    System.out.println("Vida del jugador: " + jugador[2]);
                } else {
                    jugador[2] -= 30;
                    System.out.println("¡Es una trampa!");
                    System.out.println("Vida del jugador: " + jugador[2]);
                    if (jugador[2] <= 0) {
                        System.out.println("¡Has muerto!");
                        throw new RuntimeException("El jugador murio");
                    }
                }
                mapa[x][y] = ".";
            } else if (decision.equals("no")) {
                System.out.println("Has decidido no abrir el cofre.");
            } else {
                System.out.println("Entrada no válida.");
            }
            System.out.println("-------------------------------");
        } catch (Exception e) {
            System.out.println("Error en el evento del cofre: " + e.getMessage());
        }
    }

    public static void moverJugador(String[][] mapa, String comando, int[] jugador) {
        try {
            System.out.println("-------------------------------");
            int[] posicionActual = revisarPosicionActual(mapa);
            int xActual = posicionActual[0];
            int yActual = posicionActual[1];
            int xNuevo = xActual, yNuevo = yActual;
            switch (comando) {
                case "w": xNuevo--; break;
                case "a": yNuevo--; break;
                case "s": xNuevo++; break;
                case "d": yNuevo++; break;
                default: System.out.println("Comando inválido."); return;
            }

            if (xNuevo >= 0 && xNuevo < mapa.length && yNuevo >= 0 && yNuevo < mapa[0].length) {
                if (!mapa[xNuevo][yNuevo].equals("#")) {
                    if (mapa[xNuevo][yNuevo].equals("E")) {
                        int[] enemigo = crearEnemigo(xNuevo, yNuevo);
                        if (combateEnemigo(jugador, enemigo, mapa)) {
                            xNuevo = xActual;
                            yNuevo = yActual;
                        }
                    } else if (mapa[xNuevo][yNuevo].equals("C")) {
                        eventoCofre(jugador, mapa, xNuevo, yNuevo);
                    } else if (mapa[xNuevo][yNuevo].equals("X")) {
                        System.out.println("¡Has llegado a la 'X'! ¡Has ganado el juego!");
                        System.exit(0);
                    }
                    if (!mapa[xNuevo][yNuevo].equals("C")) {
                        mapa[xActual][yActual] = ".";
                        mapa[xNuevo][yNuevo] = "J";
                    }
                } else {
                    System.out.println("Movimiento bloqueado por una pared.");
                }
            } else {
                System.out.println("Movimiento fuera de los límites del mapa.");
            }
            revisarEstadoJuego(jugador, mapa);
            System.out.println("-------------------------------");
        } catch (Exception e) {
            System.out.println("Error al mover jugador: " + e.getMessage());
        }
    }

    public static boolean combateEnemigo(int[] jugador, int[] enemigo, String[][] mapa) {
        try {
            System.out.println("*-*-*-*-*-**-*-*-*-*-*-*-*-*-*-*");
            System.out.println("Combate contra un enemigo.");
            System.out.println("*-*-*-*-*-**-*-*-*-*-*-*-*-*-*-*");
            Scanner scanner = new Scanner(System.in);
            int opcion;
            int vidaEnemigo = enemigo[2];
            int ataqueEnemigo = enemigo[3];
            while (true) {
                System.out.println("Vida jugador: " + jugador[2]);
                System.out.println("Vida enemigo: " + vidaEnemigo);
                System.out.println("-------------------------------");
                System.out.println("¿Desea atacar (1) o huir (2)?");
                System.out.println("Ingrese una opción (1 o 2): ");
                System.out.println("-------------------------------");
                if (scanner.hasNextInt()) {
                    opcion = scanner.nextInt();
                    scanner.nextLine(); // Consume the newline character
                    if (opcion == 1) {
                        jugador[2] -= ataqueEnemigo;
                        vidaEnemigo -= jugador[3];
                        System.out.println("----------------");
                        if (vidaEnemigo <= 0) {
                            System.out.println("¡El enemigo ha sido derrotado!");
                            break;
                        } else if (jugador[2] <= 0) {
                            System.out.println("¡Has sido derrotado!");
                            throw new RuntimeException("Player has been defeated");
                        } else {
                            System.out.println("¡Sigue el combate!");
                            System.out.println("-------------------------------");
                        }
                    } else if (opcion == 2) {
                        System.out.println("¡Has huido del enemigo!");
                        System.out.println("-------------------------------");
                        return true;
                    } else {
                        System.out.println("Opción inválida. Intente nuevamente.");
                        System.out.println("-------------------------------");
                    }
                } else {
                    System.out.println("Entrada no válida. Intente nuevamente.");
                    System.out.println("-------------------------------");
                    scanner.next();
                }
            }
            revisarEstadoJuego(jugador, mapa);
            System.out.println("-------------------------------");
            return false;
        } catch (Exception e) {
            System.out.println("Error en el combate con el enemigo: " + e.getMessage());
            return false;
        }
    }

    public static void llenarMapa(String[][] mapa, int[] jugador, int[] enemigo) {
        try {
            System.out.println("-------------------------------");
            mapa[0] = new String[]{"#", "#", "#", "#", "#", "#", "#", "#", "#", "#"};
            mapa[1] = new String[]{"#", ".", ".", ".", ".", ".", "#", ".", "C", "#"};
            mapa[2] = new String[]{"#", ".", ".", "#", ".", ".", ".", ".", ".", "#"};
            mapa[3] = new String[]{"#", ".", "#", ".", "C", ".", "#", "#", ".", "#"};
            mapa[4] = new String[]{"#", ".", "#", ".", ".", ".", "C", "#", ".", "#"};
            mapa[5] = new String[]{"#", ".", ".", ".", ".", ".", ".", "#", "E", "#"};
            mapa[6] = new String[]{"#", "E", ".", ".", "#", "#", ".", "#", ".", "#"};
            mapa[7] = new String[]{"#", ".", ".", "#", "#", ".", ".", "#", ".", "#"};
            mapa[8] = new String[]{"#", ".", ".", "E", ".", ".", ".", ".", "X", "#"};
            mapa[9] = new String[]{"#", "#", "#", "#", "#", "#", "#", "#", "#", "#"};
            mapa[jugador[0]][jugador[1]] = "J"; // Colocar al jugador
            mapa[enemigo[0]][enemigo[1]] = "E"; // Colocar un enemigo
            System.out.println("-------------------------------");
        } catch (Exception e) {
            System.out.println("Error al llenar el mapa: " + e.getMessage());
        }
    }

    public static void imprimirMapa(String[][] mapa) {
        try {
            System.out.println("-------------------------------");
            for (int i = 0; i < mapa.length; i++) {
                for (int j = 0; j < mapa[i].length; j++) {
                    System.out.print(mapa[i][j] + " ");
                }
                System.out.println();
            }
            System.out.println("-------------------------------");
        } catch (Exception e) {
            System.out.println("Error al imprimir el mapa: " + e.getMessage());
        }
    }

    public static Scanner abrirScanner() {
        try {
            System.out.println("-------------------------------");
            return new Scanner(System.in);
        } catch (Exception e) {
            System.out.println("Error al abrir el scanner: " + e.getMessage());
            return null;
        }
    }
}