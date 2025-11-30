import java.awt.*;

/**
 * Abstract base class for all ghosts in the Pac-Man game.
 * Ghosts extend Block to maintain compatibility with existing game mechanics.
 * Each ghost type implements its own unique chasing behavior.
 */
public abstract class Ghost extends Block {
    
    /**
     * Constructor for Ghost.
     * 
     * @param game Reference to the PacMan game instance
     * @param image The image to display for this ghost
     * @param x Initial x position
     * @param y Initial y position
     * @param width Width of the ghost
     * @param height Height of the ghost
     */
    public Ghost(PacMan game, Image image, int x, int y, int width, int height) {
        super(game, image, x, y, width, height);
    }
    
    /**
     * Calculates the target position for this ghost based on its unique behavior.
     * Each ghost type implements this differently.
     * 
     * @return A Point representing the target x,y coordinates
     */
    public abstract Point calculateTarget();
    
    /**
     * Chooses the best direction to move towards the target.
     * Considers walls and current position.
     * 
     * @return The chosen direction ('U', 'D', 'L', or 'R')
     */
    public abstract char chooseDirection();
    
    /**
     * Moves the ghost based on its AI behavior.
     * This method is called each game frame to update ghost position.
     */
    public abstract void move();
}

