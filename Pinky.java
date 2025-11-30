import java.awt.*;

/**
 * Pinky - The smart one (Pink Ghost).
 * Pinky chases Pac-Man, but it aims for a location 2 dots ahead of Pac-Man,
 * thus trying to anticipate movement and cut off Pac-Man's path.
 */
public class Pinky extends Ghost {
    
    /**
     * Constructor for Pinky (Pink Ghost).
     * 
     * @param game Reference to the PacMan game instance
     * @param image The image to display for Pinky
     * @param x Initial x position
     * @param y Initial y position
     * @param width Width of the ghost
     * @param height Height of the ghost
     */
    public Pinky(PacMan game, Image image, int x, int y, int width, int height) {
        super(game, image, x, y, width, height);
    }
    
    /**
     * Calculates the target position for Pinky.
     * Pinky's target is 2 dots ahead of Pac-Man in the direction Pac-Man is moving.
     * 
     * @return A Point representing the anticipated position 2 dots ahead of Pac-Man
     */
    @Override
    public Point calculateTarget() {
        // TODO: Implement anticipatory chase behavior
        // Get Pac-Man's current position and direction
        // Calculate position 2 dots (tiles) ahead in Pac-Man's current direction
        // Return that as the target
        if (game != null && game.getPacman() != null) {
            Block pacman = game.getPacman();
            return getAnticipatedPosition(pacman);
        }
        return new Point(x, y);
    }
    
    /**
     * Calculates the anticipated position 2 dots ahead of Pac-Man.
     * 
     * @param pacman The Pac-Man block to anticipate
     * @return A Point 2 dots ahead of Pac-Man in its current direction
     */
    public Point getAnticipatedPosition(Block pacman) {
        // TODO: Implement position anticipation
        // Based on Pac-Man's direction, calculate where it will be 2 tiles ahead
        // Consider: U = up (y decreases), D = down (y increases)
        //           L = left (x decreases), R = right (x increases)
        // Use tileSize to calculate 2 dots ahead
        int tileSize = game != null ? game.getTileSize() : 32;
        int aheadDistance = tileSize * 2; // 2 dots ahead
        
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
    
    /**
     * Chooses the best direction to move towards the anticipated target position.
     * Considers walls and chooses the direction that gets closest to the target.
     * 
     * @return The chosen direction ('U', 'D', 'L', or 'R')
     */
    @Override
    public char chooseDirection() {
        // TODO: Implement direction selection logic
        // Calculate which direction (U, D, L, R) will get Pinky closest to the anticipated target
        // Consider walls and avoid reversing direction if possible
        // For now, return current direction (ghosts won't move yet)
        return direction;
    }
    
    /**
     * Moves Pinky based on anticipatory chase behavior.
     * This method is called each game frame to update Pinky's position.
     * Currently disabled - ghosts should not move yet.
     */
    @Override
    public void move() {
        // TODO: Implement movement logic
        // 1. Calculate target using calculateTarget() (which uses getAnticipatedPosition())
        // 2. Choose direction using chooseDirection()
        // 3. Update direction and move towards target
        // 4. Handle wall collisions
        // Currently disabled - ghosts should not move yet
    }
}

