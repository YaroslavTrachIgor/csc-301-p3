import java.awt.*;

/*
Yaroslav Trach and Darwin Prowant
CSC 301
Program 3
Last Edited: 12/04/2025

This class is the Clyde ghost. It is a subclass of the Ghost class. It is responsible for the Clyde ghost's movement, its scared behavior and uses the A* pathfinding algorithm to find the shortest path to the target.
*/
public class Clyde extends Ghost {
    
    // MARK: - Initialization
    public Clyde(PacMan game, Image image, int x, int y, int width, int height) {
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
            double distance = getDistanceToPacMan();
            int tileSize = game.getTileSize();
            int scaredDistance = tileSize * 8;
            
            if (distance > scaredDistance) {
                Block pacman = game.getPacman();
                return new Point(pacman.x, pacman.y);
            } else {
                return getCornerTarget();
            }
        }
        return new Point(x, y);
    }
    
    // MARK: - Private methods
    private double getDistanceToPacMan() {
        if (game != null && game.getPacman() != null) {
            Block pacman = game.getPacman();
            int dx = pacman.x - this.x;
            int dy = pacman.y - this.y;
            return Math.sqrt(dx * dx + dy * dy);
        }
        return Double.MAX_VALUE;
    }
    
    private Point getCornerTarget() {
        if (game != null && game.getPacman() != null) {
            Block pacman = game.getPacman();
            int tileSize = game.getTileSize();
            int boardWidth = game.getBoardWidth();
            int boardHeight = game.getBoardHeight();
            
            Point[] corners = {
                new Point(0, 0),
                new Point(boardWidth - tileSize, 0),
                new Point(0, boardHeight - tileSize),
                new Point(boardWidth - tileSize, boardHeight - tileSize)
            };
            
            Point farthestCorner = corners[0];
            double maxDistance = 0;
            
            for (Point corner : corners) {
                int dx = corner.x - pacman.x;
                int dy = corner.y - pacman.y;
                double distance = Math.sqrt(dx * dx + dy * dy);
                
                if (distance > maxDistance) {
                    maxDistance = distance;
                    farthestCorner = corner;
                }
            }
            
            return farthestCorner;
        }
        return new Point(x, y);
    }
}
