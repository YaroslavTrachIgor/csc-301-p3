import java.awt.*;

/**
 * Blinky - The direct one (Red Ghost).
 * Blinky chases Pac-Man based on current locations.
 * This is the most straightforward chasing behavior.
 */
public class Blinky extends Ghost {
    
    /**
     * Constructor for Blinky (Red Ghost).
     * 
     * @param game Reference to the PacMan game instance
     * @param image The image to display for Blinky
     * @param x Initial x position
     * @param y Initial y position
     * @param width Width of the ghost
     * @param height Height of the ghost
     */
    public Blinky(PacMan game, Image image, int x, int y, int width, int height) {
        super(game, image, x, y, width, height);
    }
    
    /**
     * Calculates the target position for Blinky.
     * Blinky's target is simply Pac-Man's current position.
     * 
     * @return A Point representing Pac-Man's current x,y coordinates
     */
    @Override
    public Point calculateTarget() {
        // TODO: Implement direct chase behavior
        // Return Pac-Man's current position as the target
        if (game != null && game.getPacman() != null) {
            return new Point(game.getPacman().x, game.getPacman().y);
        }
        return new Point(x, y);
    }
    
    /**
     * Chooses the best direction to move towards Pac-Man's current position.
     * Considers walls and chooses the direction that gets closest to the target.
     * 
     * @return The chosen direction ('U', 'D', 'L', or 'R')
     */
    @Override
    public char chooseDirection() {
        // TODO: Implement direction selection logic
        // Calculate which direction (U, D, L, R) will get Blinky closest to Pac-Man
        // Consider walls and avoid reversing direction if possible
        // For now, return current direction (ghosts won't move yet)
        return direction;
    }
    
    /**
     * Moves Blinky based on direct chase behavior.
     * This method is called each game frame to update Blinky's position.
     * Currently disabled - ghosts should not move yet.
     */
    @Override
    public void move() {
        // TODO: Implement movement logic
        // 1. Calculate target using calculateTarget()
        // 2. Choose direction using chooseDirection()
        // 3. Update direction and move towards target
        // 4. Handle wall collisions
        // Currently disabled - ghosts should not move yet
    }
}

