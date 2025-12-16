import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class Board extends JPanel implements ActionListener {
    private Timer timer;
    private Pacman pacman;
    private Ghost[] ghosts;
    
    // Dimensiones del tablero
    private static final int BLOCK_SIZE = 20;
    private static final int BOARD_WIDTH = 20;
    private static final int BOARD_HEIGHT = 20;
    
    // Nivel actual y mapas de niveles
    private int currentLevel = 0;
    private int[][][] levelMaps;
    private List<Point> pellets;
    private int totalPellets;
    
    // Símbolos para el mapa: 0 = vacío, 1 = pared, 2 = pellet
    
    public Board() {
        setFocusable(true);
        setBackground(Color.BLACK);
        initializeLevels();
        initializeLevel(0);
        timer = new Timer(40, this);
        timer.start();
        addKeyListener(new PacmanKeyAdapter());
    }
    
    private void initializeLevels() {
        levelMaps = new int[3][BOARD_HEIGHT][BOARD_WIDTH];
        
        // Nivel 1 - Diseño simple con paredes exteriores y algunas paredes internas
        int[][] level1 = {
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
            {1,2,2,2,2,2,2,2,2,1,1,2,2,2,2,2,2,2,2,1},
            {1,2,1,1,2,1,1,1,2,1,1,2,1,1,1,2,1,1,2,1},
            {1,2,1,1,2,1,1,1,2,1,1,2,1,1,1,2,1,1,2,1},
            {1,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,1},
            {1,2,1,1,2,1,2,1,1,1,1,1,1,2,1,2,1,1,2,1},
            {1,2,2,2,2,1,2,2,2,1,1,2,2,2,1,2,2,2,2,1},
            {1,1,1,1,2,1,1,1,0,1,1,0,1,1,1,2,1,1,1,1},
            {1,1,1,1,2,1,0,0,0,0,0,0,0,0,1,2,1,1,1,1},
            {1,1,1,1,2,1,0,1,1,0,0,1,1,0,1,2,1,1,1,1},
            {0,0,0,0,2,0,0,1,0,0,0,0,1,0,0,2,0,0,0,0},
            {1,1,1,1,2,1,0,1,1,1,1,1,1,0,1,2,1,1,1,1},
            {1,1,1,1,2,1,0,0,0,0,0,0,0,0,1,2,1,1,1,1},
            {1,1,1,1,2,1,0,1,1,1,1,1,1,0,1,2,1,1,1,1},
            {1,2,2,2,2,2,2,2,2,1,1,2,2,2,2,2,2,2,2,1},
            {1,2,1,1,2,1,1,1,2,1,1,2,1,1,1,2,1,1,2,1},
            {1,2,2,1,2,2,2,2,2,2,2,2,2,2,2,2,1,2,2,1},
            {1,1,2,1,2,1,2,1,1,1,1,1,1,2,1,2,1,2,1,1},
            {1,2,2,2,2,1,2,2,2,1,1,2,2,2,1,2,2,2,2,1},
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}
        };
        
        // Nivel 2 - Diseño con más paredes internas
        int[][] level2 = {
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
            {1,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,1},
            {1,2,1,1,1,2,1,1,2,1,1,2,1,1,2,1,1,1,2,1},
            {1,2,2,2,2,2,1,1,2,1,1,2,1,1,2,2,2,2,2,1},
            {1,2,1,1,1,2,2,2,2,2,2,2,2,2,2,1,1,1,2,1},
            {1,2,1,1,1,2,1,1,1,1,1,1,1,1,2,1,1,1,2,1},
            {1,2,2,2,2,2,2,2,2,1,1,2,2,2,2,2,2,2,2,1},
            {1,2,1,1,1,1,1,1,2,1,1,2,1,1,1,1,1,1,2,1},
            {1,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,1},
            {1,1,1,2,1,1,2,1,1,1,1,1,1,2,1,1,2,1,1,1},
            {1,1,1,2,1,1,2,1,1,1,1,1,1,2,1,1,2,1,1,1},
            {1,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,1},
            {1,2,1,1,1,1,1,1,2,1,1,2,1,1,1,1,1,1,2,1},
            {1,2,2,2,2,2,2,2,2,1,1,2,2,2,2,2,2,2,2,1},
            {1,2,1,1,1,2,1,1,1,1,1,1,1,1,2,1,1,1,2,1},
            {1,2,1,1,1,2,2,2,2,2,2,2,2,2,2,1,1,1,2,1},
            {1,2,2,2,2,2,1,1,2,1,1,2,1,1,2,2,2,2,2,1},
            {1,2,1,1,1,2,1,1,2,1,1,2,1,1,2,1,1,1,2,1},
            {1,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,1},
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}
        };
        
        // Nivel 3 - Diseño complejo con patrón laberíntico
        int[][] level3 = {
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
            {1,2,2,2,2,2,2,2,2,1,1,2,2,2,2,2,2,2,2,1},
            {1,2,1,1,1,1,1,1,2,1,1,2,1,1,1,1,1,1,2,1},
            {1,2,2,2,1,2,2,2,2,2,2,2,2,2,1,2,2,2,2,1},
            {1,1,1,2,1,2,1,1,1,1,1,1,1,2,1,2,1,1,1,1},
            {1,2,2,2,2,2,2,2,2,1,1,2,2,2,2,2,2,2,2,1},
            {1,2,1,1,1,1,1,1,2,1,1,2,1,1,1,1,1,1,2,1},
            {1,2,1,2,2,2,2,1,2,2,2,2,1,2,2,2,2,1,2,1},
            {1,2,1,2,1,1,2,1,1,1,1,1,1,2,1,1,2,1,2,1},
            {1,2,2,2,1,1,2,2,2,1,1,2,2,2,1,1,2,2,2,1},
            {1,2,1,2,2,2,1,1,2,1,1,2,1,1,2,2,2,1,2,1},
            {1,2,1,1,1,2,2,2,2,2,2,2,2,2,2,1,1,1,2,1},
            {1,2,2,2,1,1,1,1,2,1,1,2,1,1,1,1,2,2,2,1},
            {1,1,1,2,2,2,2,1,2,1,1,2,1,2,2,2,2,1,1,1},
            {1,2,2,2,1,1,2,1,2,2,2,2,1,2,1,1,2,2,2,1},
            {1,2,1,1,1,1,2,1,1,1,1,1,1,2,1,1,1,1,2,1},
            {1,2,1,2,2,2,2,2,2,1,1,2,2,2,2,2,2,1,2,1},
            {1,2,1,2,1,1,1,1,2,1,1,2,1,1,1,1,2,1,2,1},
            {1,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,1},
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}
        };
        
        levelMaps[0] = level1;
        levelMaps[1] = level2;
        levelMaps[2] = level3;
    }
    
    private void initializeLevel(int level) {
        currentLevel = level;
        pellets = new ArrayList<>();
        totalPellets = 0;
        
        // Cargar pellets del nivel actual
        for (int row = 0; row < BOARD_HEIGHT; row++) {
            for (int col = 0; col < BOARD_WIDTH; col++) {
                if (levelMaps[currentLevel][row][col] == 2) {
                    pellets.add(new Point(col * BLOCK_SIZE, row * BLOCK_SIZE));
                    totalPellets++;
                }
            }
        }
        
        // Resetear posición de Pacman y fantasmas
        pacman = new Pacman(BLOCK_SIZE, BLOCK_SIZE);
        ghosts = new Ghost[] {
            new Ghost(9 * BLOCK_SIZE, 9 * BLOCK_SIZE, Color.RED),
            new Ghost(10 * BLOCK_SIZE, 9 * BLOCK_SIZE, Color.PINK),
            new Ghost(9 * BLOCK_SIZE, 10 * BLOCK_SIZE, Color.CYAN)
        };
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawBoard(g);
        pacman.draw(g);
        for (Ghost ghost : ghosts) {
            ghost.draw(g);
        }
    }

    private void drawBoard(Graphics g) {
        // Dibujar paredes
        g.setColor(Color.BLUE);
        for (int row = 0; row < BOARD_HEIGHT; row++) {
            for (int col = 0; col < BOARD_WIDTH; col++) {
                if (levelMaps[currentLevel][row][col] == 1) {
                    g.fillRect(col * BLOCK_SIZE, row * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
                }
            }
        }
        
        // Dibujar pellets
        g.setColor(Color.WHITE);
        for (Point pellet : pellets) {
            g.fillOval(pellet.x + 7, pellet.y + 7, 6, 6);
        }
        
        // Dibujar información del juego
        g.setColor(Color.YELLOW);
        g.drawString("Score: " + pacman.getScore(), 10, BOARD_HEIGHT * BLOCK_SIZE + 15);
        g.drawString("Level: " + (currentLevel + 1), 150, BOARD_HEIGHT * BLOCK_SIZE + 15);
        g.drawString("Pellets: " + pellets.size() + "/" + totalPellets, 250, BOARD_HEIGHT * BLOCK_SIZE + 15);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        pacman.move();
        checkPacmanWallCollision();
        checkPelletCollection();
        
        for (Ghost ghost : ghosts) {
            ghost.move();
            checkGhostWallCollision(ghost);
        }
        
        // Verificar si se completó el nivel
        if (pellets.isEmpty() && currentLevel < 2) {
            initializeLevel(currentLevel + 1);
        }
        
        repaint();
    }
    
    private void checkPacmanWallCollision() {
        int pacmanGridX = pacman.getX() / BLOCK_SIZE;
        int pacmanGridY = pacman.getY() / BLOCK_SIZE;
        
        if (pacmanGridX >= 0 && pacmanGridX < BOARD_WIDTH && 
            pacmanGridY >= 0 && pacmanGridY < BOARD_HEIGHT) {
            if (levelMaps[currentLevel][pacmanGridY][pacmanGridX] == 1) {
                pacman.undoMove();
            }
        } else {
            // Fuera de límites, revertir movimiento
            pacman.undoMove();
        }
    }
    
    private void checkGhostWallCollision(Ghost ghost) {
        int ghostGridX = ghost.getX() / BLOCK_SIZE;
        int ghostGridY = ghost.getY() / BLOCK_SIZE;
        
        if (ghostGridX >= 0 && ghostGridX < BOARD_WIDTH && 
            ghostGridY >= 0 && ghostGridY < BOARD_HEIGHT) {
            if (levelMaps[currentLevel][ghostGridY][ghostGridX] == 1) {
                ghost.undoMove();
                ghost.changeDirection();
            }
        } else {
            // Fuera de límites, revertir movimiento
            ghost.undoMove();
            ghost.changeDirection();
        }
    }
    
    private void checkPelletCollection() {
        int pacmanX = pacman.getX();
        int pacmanY = pacman.getY();
        
        // Revisar si Pacman está sobre un pellet
        pellets.removeIf(pellet -> {
            int distance = Math.abs(pellet.x - pacmanX) + Math.abs(pellet.y - pacmanY);
            if (distance < BLOCK_SIZE) {
                pacman.incrementScore(10);
                return true;
            }
            return false;
        });
    }

    private class PacmanKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            pacman.keyPressed(e);
        }
    }
}