import java.awt.*;
import java.util.*;

public class Pinky extends Ghost {
    
    public Pinky(PacMan game, Image image, int x, int y, int width, int height) {
        super(game, image, x, y, width, height);
    }
    
    @Override
    public Point calculateTarget() {
        if (game != null && game.getPacman() != null) {
            Block pacman = game.getPacman();
            return getAnticipatedPosition(pacman);
        }
        return new Point(x, y);
    }
    
    public Point getAnticipatedPosition(Block pacman) {
        int tileSize = game != null ? game.getTileSize() : 32;
        int aheadDistance = tileSize * 2;
        
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
    
    @Override
    public char chooseDirection() {
        Point target = calculateTarget();
        return findPathAStar(target);
    }
    
    @Override
    public void move() {
        char bestDirection = chooseDirection();
        updateDirection(bestDirection);
        this.velocityX = this.velocityX / 2;
        this.velocityY = this.velocityY / 2;
    }

    private char findPathAStar(Point target) {
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
                return getFirstDirection(current);
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

    private int heuristic(int row1, int col1, int row2, int col2) {
        int dx = col2 - col1;
        int dy = row2 - row1;
        return (int) Math.sqrt(dx * dx + dy * dy);
    }

    private boolean isValidPosition(int row, int col) {
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

    private char getFirstDirection(AStarNode node) {
        AStarNode current = node;
        while (current.parent != null && current.parent.parent != null) {
            current = current.parent;
        }

        if (current.direction != ' ') {
            return current.direction;
        }

        return this.direction;
    }

    private char findValidDirection() {
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

    private boolean isDirectionValid(char direction) {
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

    private class AStarNode {
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
}
