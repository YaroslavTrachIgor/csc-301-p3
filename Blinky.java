import java.awt.*;
import java.util.*;
import java.util.List;
import java.awt.Image;
import java.awt.Point;
import java.util.*;

public class Blinky extends Ghost {

    public Blinky(PacMan game, Image image, int x, int y, int width, int height) {
        super(game, image, x, y, width, height);
    }

    @Override
    public Point calculateTarget() {
        Block pacman = game.getPacman();
        return new Point(pacman.x, pacman.y);
    }

    @Override
    public char chooseDirection() {
        Point target = calculateTarget();
        return findPathBFS(target);
    }

    @Override
    public void move() {
        char bestDirection = chooseDirection();
        updateDirection(bestDirection);
        this.velocityX = this.velocityX / 2;
        this.velocityY = this.velocityY / 2;
    }



    private char findPathBFS(Point target) {
        int tileSize = game.getTileSize();

        int startRow = this.y / tileSize;
        int startCol = this.x / tileSize;
        int targetRow = target.y / tileSize;
        int targetCol = target.x / tileSize;

        Queue<Node> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();

        Node startNode = new Node(startRow, startCol, null, ' ');
        queue.offer(startNode);
        visited.add(startRow + "," + startCol);

        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        char[] directionChars = {'U', 'D', 'L', 'R'};

        while (!queue.isEmpty()) {
            Node current = queue.poll();

            if (current.row == targetRow && current.col == targetCol) {
                return getFirstDirection(current);
            }

            for (int i = 0; i < 4; i++) {
                int newRow = current.row + directions[i][0];
                int newCol = current.col + directions[i][1];
                String posKey = newRow + "," + newCol;

                if (isValidPosition(newRow, newCol) && !visited.contains(posKey)) {
                    visited.add(posKey);
                    Node newNode = new Node(newRow, newCol, current, directionChars[i]);
                    queue.offer(newNode);
                }
            }
        }

        return findValidDirection();
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

    private char getFirstDirection(Node node) {
        Node current = node;
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
        Random random = new Random();

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

    private class Node {
        int row;
        int col;
        Node parent;
        char direction;

        Node(int row, int col, Node parent, char direction) {
            this.row = row;
            this.col = col;
            this.parent = parent;
            this.direction = direction;
        }
    }
}
