import java.awt.*;
import java.util.*;

/*
 Yaroslav Trach
 CSC 301
 Program 3
 Last Edited: 12/04/2025

 This class is the main Ghost object. It is an abstract class that contains the shared pathfinding algorithms (BFS and A*) and helper methods that are reused by all ghost implementations.
 Also, it declares the reusable node classes for pathfinding algorithms.

 ACADEMIC CITATION:
 Obregon, A. (2025). Pathfinding with the A-star algorithm in Java. Medium.
 https://medium.com/@AlexanderObregon/pathfinding-with-the-a-star-algorithm-in-java-3a66446a2352
 */
public abstract class Ghost extends Block {
    
    // MARK: - Breadth-First Search Node class
    protected static class BFSNode {
        int row;
        int col;
        BFSNode parent;
        char direction;

        BFSNode(int row, int col, BFSNode parent, char direction) {
            this.row = row;
            this.col = col;
            this.parent = parent;
            this.direction = direction;
        }
    }

    // MARK: - A-Star Node class
    protected static class AStarNode {
        int row;
        int col;
        AStarNode parent;
        char direction;
        int g;
        int h;
        int f;

        AStarNode(int row, int col, AStarNode parent, char direction, int g, int h) {
            this.row = row;
            this.col = col;
            this.parent = parent;
            this.direction = direction;
            this.g = g;
            this.h = h;
            this.f = g + h;
        }
    }
    
    // MARK: - Initialization
    public Ghost(PacMan game, Image image, int x, int y, int width, int height) {
        super(game, image, x, y, width, height);
    }
    
    //MARK: - Abstract methods
    /*
     * Calculates the target position for this ghost based on its unique behavior.
     * Each ghost type implements this differently.
     * 
     * @return A Point representing the target x,y coordinates
     */
    public abstract Point calculateTarget();

    /*
     * Chooses the best direction to move towards the target.
     * Considers walls and current position.
     *
     * @return The chosen direction ('U', 'D', 'L', or 'R')
     */
    public abstract char chooseDirection();

    /*
     * Moves the ghost based on its AI behavior.
     * This method is called each game frame to update ghost position.
     */
    public abstract void move();

    // MARK: - Protected methods
    /*
    In the function below, I implement the Breadth-First Search (BFS) algorithm to find the shortest path to the target.
    This algorithm is used for Blinky and Inky ghosts.

    The algorithm operates using a queue data structure (FIFO - First In First Out), which ensures
    that nodes are processed in the order they were discovered. This queue-based approach is the
    fundamental BFS principle: nodes discovered earlier are explored before nodes discovered later,
    creating a level by level like expansion from the starting position.

    The algorithm also maintains a `visited` set to prevent revisiting nodes, which is crucial for both
    correctness and efficiency. Without this, the algorithm could get stuck in infinite loops or
    explore the same nodes multiple times unnecessarily.
     */
    protected char findPathBFS(Point target) {
        int tileSize = game.getTileSize();

        int startRow = this.y / tileSize;
        int startCol = this.x / tileSize;
        int targetRow = target.y / tileSize;
        int targetCol = target.x / tileSize;

        Queue<BFSNode> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();

        BFSNode startNode = new BFSNode(startRow, startCol, null, ' ');
        queue.offer(startNode);
        visited.add(startRow + "," + startCol);

        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        char[] directionChars = {'U', 'D', 'L', 'R'};

        while (!queue.isEmpty()) {
            BFSNode current = queue.poll();

            if (current.row == targetRow && current.col == targetCol) {
                return getFirstDirectionBFS(current);
            }

            for (int i = 0; i < 4; i++) {
                int newRow = current.row + directions[i][0];
                int newCol = current.col + directions[i][1];
                String posKey = newRow + "," + newCol;

                if (isValidPosition(newRow, newCol) && !visited.contains(posKey)) {
                    visited.add(posKey);
                    BFSNode newNode = new BFSNode(newRow, newCol, current, directionChars[i]);
                    queue.offer(newNode);
                }
            }
        }

        return findValidDirection();
    }

    /*
     In the function below, I implement the A-Star algorithm to find the shortest path to the target
     (See header for algorithm research reference).
     This algorithm is used for Pinky, Clyde, and Inky ghosts.

     The algorithm operates using a priority queue data structure (FIFO - First In First Out), which ensures
     that nodes are processed in the order of their estimated total cost (actual cost so far + estimated remaining cost).
     This is the A* algorithm's main principle: nodes with the lowest estimated total cost are explored first.
     */
    protected char findPathAStar(Point target) {
        int tileSize = game.getTileSize();

        int startRow = this.y / tileSize;
        int startCol = this.x / tileSize;
        int targetRow = target.y / tileSize;
        int targetCol = target.x / tileSize;

        PriorityQueue<AStarNode> openSet = new PriorityQueue<>((a, b) -> {
            int fCompare = Integer.compare(a.f, b.f);
            if (fCompare != 0) return fCompare;
            return Integer.compare(a.h, b.h);
        });
        Map<String, AStarNode> allNodes = new HashMap<>();
        Set<String> closedSet = new HashSet<>();

        AStarNode startNode = new AStarNode(startRow, startCol, null, ' ', 0, heuristic(startRow, startCol, targetRow, targetCol));
        startNode.f = startNode.g + startNode.h;
        openSet.offer(startNode);
        allNodes.put(startRow + "," + startCol, startNode);

        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        char[] directionChars = {'U', 'D', 'L', 'R'};

        while (!openSet.isEmpty()) {
            AStarNode current = openSet.poll();
            String currentKey = current.row + "," + current.col;

            if (closedSet.contains(currentKey)) {
                continue;
            }

            closedSet.add(currentKey);

            if (current.row == targetRow && current.col == targetCol) {
                return getFirstDirectionAStar(current);
            }

            for (int i = 0; i < 4; i++) {
                int newRow = current.row + directions[i][0];
                int newCol = current.col + directions[i][1];
                String neighborKey = newRow + "," + newCol;

                if (!isValidPosition(newRow, newCol) || closedSet.contains(neighborKey)) {
                    continue;
                }

                int tentativeG = current.g + 1;

                AStarNode neighbor = allNodes.get(neighborKey);
                if (neighbor == null) {
                    int h = heuristic(newRow, newCol, targetRow, targetCol);
                    neighbor = new AStarNode(newRow, newCol, current, directionChars[i], tentativeG, h);
                    neighbor.f = neighbor.g + neighbor.h;
                    allNodes.put(neighborKey, neighbor);
                    openSet.offer(neighbor);
                } else if (tentativeG < neighbor.g) {
                    neighbor.g = tentativeG;
                    neighbor.f = neighbor.g + neighbor.h;
                    neighbor.parent = current;
                    neighbor.direction = directionChars[i];
                    if (!openSet.contains(neighbor)) {
                        openSet.offer(neighbor);
                    }
                }
            }
        }

        return findValidDirection();
    }

    /*
     This calculates the heuristic value (estimated cost) from one position to another using Euclidean distance.
     This heuristic function is a core component of the A* algorithm. It provides an estimate of the
     remaining cost from the current node to the goal.
     */
    protected int heuristic(int row1, int col1, int row2, int col2) {
        int dx = col2 - col1;
        int dy = row2 - row1;
        return (int) Math.sqrt(dx * dx + dy * dy);
    }

    /*
     This function checks if a grid position is valid (not a wall and within bounds).
     */
    protected boolean isValidPosition(int row, int col) {
        int tileSize = game.getTileSize();
        int x = col * tileSize;
        int y = row * tileSize;

        if (x < 0 || x >= game.getBoardWidth() || y < 0 || y >= game.getBoardHeight()) {
            return false;
        }

        for (Block wall : game.getWalls()) {
            if (wall.x == x && wall.y == y) {
                return false;
            }
        }

        return true;
    }

    /*
     This function backtracks from the target node to find the first direction taken in an BFS path.
     This is used to determine the direction of the ghost after the BFS algorithm has found the shortest path.
     */
    protected char getFirstDirectionBFS(BFSNode node) {
        BFSNode current = node;
        while (current.parent != null && current.parent.parent != null) {
            current = current.parent;
        }

        if (current.direction != ' ') {
            return current.direction;
        }

        return this.direction;
    }

    /*
     This function backtracks from the target node to find the first direction taken in an A* path.
     This is used to determine the direction of the ghost after the A* algorithm has found the shortest path.
     */
    protected char getFirstDirectionAStar(AStarNode node) {
        AStarNode current = node;
        while (current.parent != null && current.parent.parent != null) {
            current = current.parent;
        }

        if (current.direction != ' ') {
            return current.direction;
        }

        return this.direction;
    }

    /*
     This function finds a valid direction to move (fallback when pathfinding fails).
     This is used to determine the direction of the ghost when the pathfinding algorithms have failed.
     */
    protected char findValidDirection() {
        char[] directions = {'U', 'D', 'L', 'R'};

        if (isDirectionValid(this.direction)) {
            return this.direction;
        }

        for (char dir : directions) {
            if (isDirectionValid(dir)) {
                return dir;
            }
        }

        return this.direction;
    }

    /*
     This function checks if a direction is valid from current position (no walls, within bounds).
     This is used to determine if the direction is valid for the ghost to move in.
     */
    protected boolean isDirectionValid(char direction) {
        int tileSize = game.getTileSize();
        int testX = this.x;
        int testY = this.y;

        switch (direction) {
            case 'U': testY -= tileSize; break;
            case 'D': testY += tileSize; break;
            case 'L': testX -= tileSize; break;
            case 'R': testX += tileSize; break;
        }

        if (testX < 0 || testX >= game.getBoardWidth() || testY < 0 || testY >= game.getBoardHeight()) {
            return false;
        }

        for (Block wall : game.getWalls()) {
            if (wall.x == testX && wall.y == testY) {
                return false;
            }
        }

        return true;
    }
}
