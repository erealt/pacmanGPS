import java.awt.*;
import java.awt.event.*;

public class Pacman {
    private int x, y;
    private int previousX, previousY;
    private Direction direction = Direction.LEFT;
    private int score = 0;

    public Pacman(int x, int y) {
        this.x = x;
        this.y = y;
        this.previousX = x;
        this.previousY = y;
    }

    public void draw(Graphics g) {
        g.setColor(Color.YELLOW);
        g.fillArc(x, y, 20, 20, direction.getAngle(), 300);
    }

    public void move() {
        previousX = x;
        previousY = y;
        
        switch (direction) {
            case LEFT: x -= 4; break;
            case RIGHT: x += 4; break;
            case UP: y -= 4; break;
            case DOWN: y += 4; break;
        }
    }
    
    public void undoMove() {
        x = previousX;
        y = previousY;
    }

    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT: direction = Direction.LEFT; break;
            case KeyEvent.VK_RIGHT: direction = Direction.RIGHT; break;
            case KeyEvent.VK_UP: direction = Direction.UP; break;
            case KeyEvent.VK_DOWN: direction = Direction.DOWN; break;
        }
    }

    public int getScore() {
        return score;
    }
    
    public void incrementScore(int points) {
        score += points;
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
}