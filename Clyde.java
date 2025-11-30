import java.awt.*;

/**
 * Clyde - The scaredy-cat (Orange Ghost).
 * Clyde will chase Pac-Man directly, but when it gets within 8 dots,
 * it gets scared and runs away to a corner or safe location.
 */
public class Clyde extends Ghost {
    
    /**
     * Constructor for Clyde (Orange Ghost).
     * 
     * @param game Reference to the PacMan game instance
     * @param image The image to display for Clyde
     * @param x Initial x position
     * @param y Initial y position
     * @param width Width of the ghost
     * @param height Height of the ghost
     */
    public Clyde(PacMan game, Image image, int x, int y, int width, int height) {
        super(game, image, x, y, width, height);
    }
    
    /**
     * Calculates the target position for Clyde.
     * If Clyde is far from Pac-Man (>8 dots), target is Pac-Man's position.
     * If Clyde is close to Pac-Man (<=8 dots), target is a corner or safe location.
     * 
     * @return A Point representing the target position
     */
    @Override
    public Point calculateTarget() {
        // TODO: Implement scaredy-cat behavior
        // Check distance to Pac-Man using getDistanceToPacMan()
        // If distance > 8 dots: target = Pac-Man's current position (chase)
        // If distance <= 8 dots: target = corner/safe location (run away)
        if (game != null && game.getPacman() != null) {
            double distance = getDistanceToPacMan();
            int tileSize = game.getTileSize();
            int scaredDistance = tileSize * 8; // 8 dots
            
            if (distance > scaredDistance) {
                // Chase Pac-Man directly
                Block pacman = game.getPacman();
                return new Point(pacman.x, pacman.y);
            } else {
                // Get scared - run to corner
                return getCornerTarget();
            }
        }
        return new Point(x, y);
    }
    
    /**
     * Calculates the distance from Clyde to Pac-Man.
     * 
     * @return The Euclidean distance in pixels
     */
    public double getDistanceToPacMan() {
        // TODO: Calculate distance to Pac-Man
        // Use Euclidean distance: sqrt((x2-x1)^2 + (y2-y1)^2)
        if (game != null && game.getPacman() != null) {
            Block pacman = game.getPacman();
            int dx = pacman.x - this.x;
            int dy = pacman.y - this.y;
            return Math.sqrt(dx * dx + dy * dy);
        }
        return Double.MAX_VALUE;
    }
    
    /**
     * Checks if Clyde is scared (within 8 dots of Pac-Man).
     * 
     * @return true if Clyde should be scared and run away, false otherwise
     */
    public boolean isScared() {
        // TODO: Check if Clyde is within scared distance
        // Return true if distance to Pac-Man <= 8 dots
        double distance = getDistanceToPacMan();
        int tileSize = game != null ? game.getTileSize() : 32;
        int scaredDistance = tileSize * 8;
        return distance <= scaredDistance;
    }
    
    /**
     * Gets a corner or safe location target for when Clyde is scared.
     * 
     * @return A Point representing a corner position to flee to
     */
    private Point getCornerTarget() {
        // TODO: Return a corner position (e.g., bottom-left or top-right corner)
        // This is where Clyde runs when scared
        if (game != null) {
            // Default to bottom-left corner
            return new Point(0, game.getBoardHeight() - game.getTileSize());
        }
        return new Point(x, y);
    }
    
    /**
     * Chooses the best direction to move towards the target position.
     * When scared, this will move away from Pac-Man towards a corner.
     * When not scared, this will move towards Pac-Man.
     * 
     * @return The chosen direction ('U', 'D', 'L', or 'R')
     */
    @Override
    public char chooseDirection() {
        // TODO: Implement direction selection logic
        // If scared: choose direction that moves towards corner target
        // If not scared: choose direction that moves towards Pac-Man
        // Consider walls and avoid reversing direction if possible
        // For now, return current direction (ghosts won't move yet)
        return direction;
    }
    
    /**
     * Moves Clyde based on scaredy-cat behavior.
     * This method is called each game frame to update Clyde's position.
     * Currently disabled - ghosts should not move yet.
     */
    @Override
    public void move() {
        // TODO: Implement movement logic
        // 1. Check if scared using isScared()
        // 2. Calculate target using calculateTarget()
        // 3. Choose direction using chooseDirection()
        // 4. Update direction and move towards target (or away if scared)
        // 5. Handle wall collisions
        // Currently disabled - ghosts should not move yet
    }
}

