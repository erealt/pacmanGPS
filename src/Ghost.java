import java.awt.*;
import java.util.Random;

public class Ghost {
    private int x, y;
    private int previousX, previousY;
    private Direction direction;
    private Color color;
    private Random random = new Random();

    public Ghost(int x, int y, Color color) {
        this.x = x;
        this.y = y;
        this.previousX = x;
        this.previousY = y;
        this.color = color;
        this.direction = Direction.values()[random.nextInt(4)];
    }

    public void draw(Graphics g) {
        g.setColor(color);
        g.fillOval(x, y, 20, 20);
    }

    public void move() {
        if (random.nextInt(10) == 0) {
            direction = Direction.values()[random.nextInt(4)];
        }
        
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
    
    public void changeDirection() {
        direction = Direction.values()[random.nextInt(4)];
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
}