import java.awt.*;

/*
Yaroslav Trach
CSC 301
Program 3
Last Edited: 12/04/2025

This class is the Pinky ghost. It is a subclass of the Ghost class. It is responsible for the Pinky ghost's movement and behavior and uses the A* pathfinding algorithm to find the shortest path to the target.
*/
public class Pinky extends Ghost {
    
    // MARK: - Initialization
    public Pinky(PacMan game, Image image, int x, int y, int width, int height) {
        super(game, image, x, y, width, height);
    }

    // MARK: - Lifecycle
    @Override
    public void move() {
        char bestDirection = chooseDirection();
        updateDirection(bestDirection);
        this.velocityX = this.velocityX / 2;
        this.velocityY = this.velocityY / 2;
    }

    @Override
    public char chooseDirection() {
        Point target = calculateTarget();
        return findPathAStar(target);
    }
    
    @Override
    public Point calculateTarget() {
        if (game != null && game.getPacman() != null) {
            Block pacman = game.getPacman();
            return getAnticipatedPosition(pacman);
        }
        return new Point(x, y);
    }
    
    // MARK: - Private methods
    /*
     The function below calculates smart position to pacman
     based on the pacman's current direction 2 tiles ahead.
    */
    private Point getAnticipatedPosition(Block pacman) {
        int tileSize = game != null ? game.getTileSize() : 32;
        int aheadDistance = tileSize * 2;
        
        int targetX = pacman.x;
        int targetY = pacman.y;
        
        if (pacman.direction == 'U') {
            targetY -= aheadDistance;
        } else if (pacman.direction == 'D') {
            targetY += aheadDistance;
        } else if (pacman.direction == 'L') {
            targetX -= aheadDistance;
        } else if (pacman.direction == 'R') {
            targetX += aheadDistance;
        }
        
        return new Point(targetX, targetY);
    }
}
