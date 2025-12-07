import java.awt.*;

/*
Yaroslav Trach and Darwin Prowant
CSC 301
Program 3
Last Edited: 12/04/2025

This class is the Inky ghost, subclass og Ghost object. It uses the combination of pathfinding algorithms to find the shortest path to the target.
*/
public class Inky extends Ghost {
    
    // MARK: - Initialization
    public Inky(PacMan game, Image image, int x, int y, int width, int height) {
        super(game, image, x, y, width, height);
    }
    
    // MARK: - Lifecycle
    @Override
    public Point calculateTarget() {
        if (game != null && game.getPacman() != null) {
            return combineStrategies();
        }
        return new Point(x, y);
    }
    
    @Override
    public char chooseDirection() {
        Point target = calculateTarget();
        return findPathCombination(target);
    }
    
    @Override
    public void move() {
        char bestDirection = chooseDirection();
        updateDirection(bestDirection);
        this.velocityX = this.velocityX / 2;
        this.velocityY = this.velocityY / 2;
    }

    // MARK: - Private methods
    /*
     The function below combines the results of the Blinky and Pinky pathfinding algorithms
     to find the shortest path to the target. 
     
     We select the direction that is valid for Inky by checking the validity of the directions from the Blinky and Pinky pathfinding algorithms.
     I do it to simulate the combined behavior of the ghost, which is actually visible when the game is running.
    */
    private char findPathCombination(Point target) {
        Block pacman = game.getPacman();
        if (pacman == null) {
            return findValidDirection();
        }

        Point blinkyTarget = new Point(pacman.x, pacman.y);
        
        int tileSize = game.getTileSize();
        int aheadDistance = tileSize * 2;
        Point pinkyTarget = new Point(pacman.x, pacman.y);
        
        if (pacman.direction == 'U') {
            pinkyTarget.y -= aheadDistance;
        } else if (pacman.direction == 'D') {
            pinkyTarget.y += aheadDistance;
        } else if (pacman.direction == 'L') {
            pinkyTarget.x -= aheadDistance;
        } else if (pacman.direction == 'R') {
            pinkyTarget.x += aheadDistance;
        }

        char bfsDirection = findPathBFS(blinkyTarget);
        char aStarDirection = findPathAStar(pinkyTarget);

        if (bfsDirection == aStarDirection && isDirectionValid(bfsDirection)) {
            return bfsDirection;
        }

        if (isDirectionValid(aStarDirection)) {
            return aStarDirection;
        }

        if (isDirectionValid(bfsDirection)) {
            return bfsDirection;
        }

        return findValidDirection();
    }

    /*
     In the function below, I find and combine the targets of the Blinky and Pinky ghosts,
     and get the average of the two, specifically the average of the x and y coordinates between the two targets.
     */
    private Point combineStrategies() {
        Block pacman = game.getPacman();
        if (pacman == null) {
            return new Point(x, y);
        }
        
        Point blinkyTarget = new Point(pacman.x, pacman.y);
        
        int tileSize = game.getTileSize();
        int aheadDistance = tileSize * 2;
        Point pinkyTarget = new Point(pacman.x, pacman.y);
        
        if (pacman.direction == 'U') {
            pinkyTarget.y -= aheadDistance;
        } else if (pacman.direction == 'D') {
            pinkyTarget.y += aheadDistance;
        } else if (pacman.direction == 'L') {
            pinkyTarget.x -= aheadDistance;
        } else if (pacman.direction == 'R') {
            pinkyTarget.x += aheadDistance;
        }
        
        int combinedX = (blinkyTarget.x + pinkyTarget.x) / 2;
        int combinedY = (blinkyTarget.y + pinkyTarget.y) / 2;
        
        return new Point(combinedX, combinedY);
    }
}
