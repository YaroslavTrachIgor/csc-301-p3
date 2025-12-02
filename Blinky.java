import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Blinky - The direct one (Red Ghost).
 * Blinky chases Pac-Man based on current locations using BFS pathfinding.
 * This implementation uses BFS to find the shortest path to Pac-Man's current grid position
 * and selects the next direction along that path, avoiding immediate reverses.
 */
public class Blinky extends Ghost {

    private int tileSize;
    private int rows;
    private int cols;
    private boolean[][] wallGrid;

    /**
     * Constructor for Blinky (Red Ghost).
     * Precomputes the wall grid for fast BFS lookups.
     */
    public Blinky(PacMan game, Image image, int x, int y, int width, int height) {
        super(game, image, x, y, width, height);
        tileSize = game.getTileSize();
        rows = game.getBoardHeight() / tileSize;
        cols = game.getBoardWidth() / tileSize;
        wallGrid = new boolean[rows][cols];
        for (Block wall : game.getWalls()) {
            int wr = wall.y / tileSize;
            int wc = wall.x / tileSize;
            if (wr >= 0 && wr < rows && wc >= 0 && wc < cols) {
                wallGrid[wr][wc] = true;
            }
        }
    }

    /**
     * Calculates the target position for Blinky.
     * Blinky's target is simply Pac-Man's current position.
     */
    @Override
    public Point calculateTarget() {
        Block pac = game.getPacman();
        if (pac != null) {
            return new Point(pac.x, pac.y);
        }
        return new Point(x, y);
    }

    /**
     * Gets the current grid column of the ghost.
     */
    private int getCol() {
        return x / tileSize;
    }

    /**
     * Gets the current grid row of the ghost.
     */
    private int getRow() {
        return y / tileSize;
    }

    /**
     * Checks if a grid cell is a wall.
     */
    private boolean isWall(int r, int c) {
        return (r < 0 || r >= rows || c < 0 || c >= cols) || wallGrid[r][c];
    }

    /**
     * Gets the opposite direction to avoid immediate reverses.
     */
    private char getOpposite(char dir) {
        switch (dir) {
            case 'U': return 'D';
            case 'D': return 'U';
            case 'L': return 'R';
            case 'R': return 'L';
            default: return ' ';
        }
    }

    /**
     * Chooses the best direction using BFS to Pac-Man's current position.
     * Performs BFS on the grid to find the shortest path, reconstructs it,
     * and returns the direction to the next cell on the path.
     * Skips the opposite direction from the current one to avoid reversing.
     */
    @Override
    public char chooseDirection() {
        Block pac = game.getPacman();
        if (pac == null) {
            return direction;
        }

        int gcol = getCol();
        int grow = getRow();
        int pcol = pac.x / tileSize;
        int prow = pac.y / tileSize;

        if (gcol == pcol && grow == prow) {
            return direction; // Already at target
        }

        Point start = new Point(gcol, grow);
        Point goal = new Point(pcol, prow);

        Queue<Point> queue = new LinkedList<>();
        Map<Point, Point> cameFrom = new HashMap<>();
        Set<Point> visited = new HashSet<>();

        queue.offer(start);
        visited.add(start);
        cameFrom.put(start, null);

        boolean found = false;
        Point goalPoint = null;

        int[] drs = {-1, 1, 0, 0};      // U, D, L, R
        int[] dcs = {0, 0, -1, 1};
        char[] dirChars = {'U', 'D', 'L', 'R'};

        while (!queue.isEmpty() && !found) {
            Point curr = queue.poll();
            if (curr.x == goal.x && curr.y == goal.y) {
                goalPoint = curr;
                found = true;
                break;
            }

            for (int i = 0; i < 4; i++) {
                // Skip opposite direction only from start position
                if (curr.equals(start) && dirChars[i] == getOpposite(direction)) {
                    continue;
                }

                int nr = curr.y + drs[i];
                int nc = curr.x + dcs[i];

                if (!isWall(nr, nc)) {
                    Point nextPoint = new Point(nc, nr);
                    if (!visited.contains(nextPoint)) {
                        visited.add(nextPoint);
                        queue.offer(nextPoint);
                        cameFrom.put(nextPoint, curr);
                    }
                }
            }
        }

        if (goalPoint == null) {
            return direction; // No path found, keep current
        }

        // Reconstruct path
        List<Point> path = new ArrayList<>();
        Point curr = goalPoint;
        while (curr != null) {
            path.add(curr);
            curr = cameFrom.get(curr);
        }
        Collections.reverse(path);

        if (path.size() < 2) {
            return direction;
        }

        // Next position on path
        Point nextPos = path.get(1);
        int dr = nextPos.y - start.y;
        int dc = nextPos.x - start.x;

        if (dr == -1) return 'U';
        if (dr == 1) return 'D';
        if (dc == -1) return 'L';
        if (dc == 1) return 'R';

        return direction; // Fallback
    }

    /**
     * Moves Blinky: chooses direction and updates velocity.
     * Position update and collision handling should be done in PacMan.move().
     */
    @Override
    public void move() {
        char newDir = chooseDirection();
        updateDirection(newDir);
        // Position update: x += velocityX; y += velocityY; handled externally
        // Collision revert and re-choose also handled externally
    }
}

