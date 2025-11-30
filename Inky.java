import java.awt.*;

/**
 * Inky - The hybrid (Blue Ghost).
 * Inky's movement is a combination of Blinky and Pinky's strategies.
 * It uses a hybrid approach that combines direct chasing and anticipatory behavior.
 */
public class Inky extends Ghost {
    
    /**
     * Constructor for Inky (Blue Ghost).
     * 
     * @param game Reference to the PacMan game instance
     * @param image The image to display for Inky
     * @param x Initial x position
     * @param y Initial y position
     * @param width Width of the ghost
     * @param height Height of the ghost
     */
    public Inky(PacMan game, Image image, int x, int y, int width, int height) {
        super(game, image, x, y, width, height);
    }
    
    /**
     * Calculates the target position for Inky.
     * Inky's target is a combination of Blinky's direct chase and Pinky's anticipatory behavior.
     * 
     * @return A Point representing the hybrid target position
     */
    @Override
    public Point calculateTarget() {
        // TODO: Implement hybrid chase behavior
        // Combine Blinky's direct target (Pac-Man's current position) 
        // with Pinky's anticipatory target (2 dots ahead)
        // You might average them, or use a weighted combination
        if (game != null && game.getPacman() != null) {
            return combineStrategies();
        }
        return new Point(x, y);
    }
    
    /**
     * Combines Blinky's and Pinky's strategies to create a hybrid target.
     * 
     * @return A Point representing the combined target position
     */
    public Point combineStrategies() {
        // TODO: Implement strategy combination
        // Get Blinky's target (direct: Pac-Man's current position)
        // Get Pinky's target (anticipatory: 2 dots ahead)
        // Combine them (e.g., average, weighted average, or other combination)
        // Return the combined target point
        
        Block pacman = game.getPacman();
        if (pacman == null) {
            return new Point(x, y);
        }
        
        // Blinky's direct target
        Point blinkyTarget = new Point(pacman.x, pacman.y);
        
        // Pinky's anticipatory target (2 dots ahead)
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
        
        // Combine: average of both strategies
        int combinedX = (blinkyTarget.x + pinkyTarget.x) / 2;
        int combinedY = (blinkyTarget.y + pinkyTarget.y) / 2;
        
        return new Point(combinedX, combinedY);
    }
    
    /**
     * Chooses the best direction to move towards the hybrid target position.
     * Considers walls and chooses the direction that gets closest to the target.
     * 
     * @return The chosen direction ('U', 'D', 'L', or 'R')
     */
    @Override
    public char chooseDirection() {
        // TODO: Implement direction selection logic
        // Calculate which direction (U, D, L, R) will get Inky closest to the hybrid target
        // Consider walls and avoid reversing direction if possible
        // For now, return current direction (ghosts won't move yet)
        return direction;
    }
    
    /**
     * Moves Inky based on hybrid chase behavior.
     * This method is called each game frame to update Inky's position.
     * Currently disabled - ghosts should not move yet.
     */
    @Override
    public void move() {
        // TODO: Implement movement logic
        // 1. Calculate target using calculateTarget() (which uses combineStrategies())
        // 2. Choose direction using chooseDirection()
        // 3. Update direction and move towards target
        // 4. Handle wall collisions
        // Currently disabled - ghosts should not move yet
    }
}

