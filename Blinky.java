import java.awt.*;

/*
Yaroslav Trach
CSC 301
Program 3
Last Edited: 12/04/2025

This class is the Blinky ghost. It is a subclass of the Ghost class. It is responsible for the Blinky ghost's movement and behavior and uses the BFS pathfinding algorithm to find the shortest path to the target.
*/
public class Blinky extends Ghost {

    // MARK: - Initialization
    public Blinky(PacMan game, Image image, int x, int y, int width, int height) {
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
        return findPathBFS(target);
    }

    @Override
    public Point calculateTarget() {
        Block pacman = game.getPacman();
        return new Point(pacman.x, pacman.y);
    }
}
