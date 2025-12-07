import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Random;
import javax.swing.*;

/*
Yaroslav Trach and Darwin Proviant
CSC 301
Program 3
Last Edited: 12/04/2025

This class is the main Pac-Man game class. It is responsible for the game loop, drawing of the characters and asstes 

ACADEMIC CITATION:
Cursor AI. (2025). *Code generation and implementation assistance* [AI software]. Cursor. https://cursor.sh
> "Clone the open-source Pac-Man game project you see in the current directory. It will be used for the university program in which we need to write a custom code for ghosts:
>
> - **Blinky**: The direct one. It chases Pac-Man based on current locations.
> - **Pinky**: The smart one. It chases Pac-Man, but it aims for a location 2 dots ahead of Pac-Man, thus trying to anticipate movement.
> - **Inky**: The hybrid. Its movement is a combination of Blinky and Pinky.
> - **Clyde**: The scaredy-cat. It will chase Pac-Man directly, but when it gets within 8 dots it gets scared and runs.
>
> You should add custom classes for each of those ghosts so that I can program them from scratch.
> Declare all the basic methods with comments for each ghost class.
> At the end, you should come up with the same program but the ghosts should not move for now. Pac-Man object should act as it moved before."

YouTube. (n.d.). *Pacman Java Tutorial* [Video]. YouTube. https://youtu.be/lB_J-VNMVpE
*/
public class PacMan extends JPanel implements ActionListener, KeyListener {

    private int rowCount = 21;
    private int columnCount = 19;
    private int tileSize = 32;
    private int boardWidth = columnCount * tileSize;
    private int boardHeight = rowCount * tileSize;

    private Image wallImage;
    private Image blueGhostImage;
    private Image orangeGhostImage;
    private Image pinkGhostImage;
    private Image redGhostImage;

    private Image pacmanUpImage;
    private Image pacmanDownImage;
    private Image pacmanLeftImage;
    private Image pacmanRightImage;

    //X = wall, O = skip, P = pac man, ' ' = food
    //Ghosts: b = blue, o = orange, p = pink, r = red
    private String[] tileMap = {
        "XXXXXXXXXXXXXXXXXXX",
        "X        X        X",
        "X XX XXX X XXX XX X",
        "X                 X",
        "X XX X XXXXX X XX X",
        "X    X       X    X",
        "XXXX XXXX XXXX XXXX",
        "OOOX X       X XOOO",
        "XXXX X XXrXX X XXXX",
        "O       bpo       O",
        "XXXX X XXXXX X XXXX",
        "OOOX X       X XOOO",
        "XXXX X XXXXX X XXXX",
        "X        X        X",
        "X XX XXX X XXX XX X",
        "X  X     P     X  X",
        "XX X X XXXXX X X XX",
        "X    X   X   X    X",
        "X XXXXXX X XXXXXX X",
        "X                 X",
        "XXXXXXXXXXXXXXXXXXX" 
    };

    HashSet<Block> walls;
    HashSet<Block> foods;
    HashSet<Ghost> ghosts;
    Block pacman;
    char nextDirection = ' ';

    Timer gameLoop;
    char[] directions = {'U', 'D', 'L', 'R'};
    Random random = new Random();
    int score = 0;
    int lives = 3;
    boolean gameOver = false;

    PacMan() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(Color.BLACK);
        addKeyListener(this);
        setFocusable(true);

        wallImage = new ImageIcon(getClass().getResource("./wall.png")).getImage();
        blueGhostImage = new ImageIcon(getClass().getResource("./blueGhost.png")).getImage();
        orangeGhostImage = new ImageIcon(getClass().getResource("./orangeGhost.png")).getImage();
        pinkGhostImage = new ImageIcon(getClass().getResource("./pinkGhost.png")).getImage();
        redGhostImage = new ImageIcon(getClass().getResource("./redGhost.png")).getImage();

        pacmanUpImage = new ImageIcon(getClass().getResource("./pacmanUp.png")).getImage();
        pacmanDownImage = new ImageIcon(getClass().getResource("./pacmanDown.png")).getImage();
        pacmanLeftImage = new ImageIcon(getClass().getResource("./pacmanLeft.png")).getImage();
        pacmanRightImage = new ImageIcon(getClass().getResource("./pacmanRight.png")).getImage();

        loadMap();
        gameLoop = new Timer(75, this); //20fps (1000/50)
        gameLoop.start();

    }

    public void loadMap() {
        walls = new HashSet<Block>();
        foods = new HashSet<Block>();
        ghosts = new HashSet<Ghost>();

        for (int r = 0; r < rowCount; r++) {
            for (int c = 0; c < columnCount; c++) {
                String row = tileMap[r];
                char tileMapChar = row.charAt(c);

                int x = c*tileSize;
                int y = r*tileSize;

                if (tileMapChar == 'X') { //block wall
                    Block wall = new Block(this, wallImage, x, y, tileSize, tileSize);
                    walls.add(wall);
                }
                else if (tileMapChar == 'b') { //blue ghost (Inky)
                    Ghost ghost = new Inky(this, blueGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(ghost);
                }
                else if (tileMapChar == 'o') { //orange ghost (Clyde)
                    Ghost ghost = new Clyde(this, orangeGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(ghost);
                }
                else if (tileMapChar == 'p') { //pink ghost (Pinky)
                    Ghost ghost = new Pinky(this, pinkGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(ghost);
                }
                else if (tileMapChar == 'r') { //red ghost (Blinky)
                    Ghost ghost = new Blinky(this, redGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(ghost);
                }
                else if (tileMapChar == 'P') { //pacman
                    pacman = new Block(this, pacmanRightImage, x, y, tileSize, tileSize);
                }
                else if (tileMapChar == ' ') { //food
                    Block food = new Block(this, null, x + 14, y + 14, 4, 4);
                    foods.add(food);
                }
            }
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        g.drawImage(pacman.image, pacman.x, pacman.y, pacman.width, pacman.height, null);

        for (Ghost ghost : ghosts) {
            g.drawImage(ghost.image, ghost.x, ghost.y, ghost.width, ghost.height, null);
        }

        for (Block wall : walls) {
            g.drawImage(wall.image, wall.x, wall.y, wall.width, wall.height, null);
        }

        g.setColor(Color.WHITE);
        for (Block food : foods) {
            g.fillRect(food.x, food.y, food.width, food.height);
        }
        //score
        g.setFont(new Font("Arial", Font.PLAIN, 18));
        if (gameOver) {
            g.drawString("Game Over: " + String.valueOf(score), tileSize/2, tileSize/2);
        }
        else {
            g.drawString("x" + String.valueOf(lives) + " Score: " + String.valueOf(score), tileSize/2, tileSize/2);
        }
    }

    public void move() {
        // Check if we can change direction (when aligned to grid)
        if (nextDirection != ' ' && canChangeDirection(nextDirection)) {
            pacman.direction = nextDirection;
            pacman.updateVelocity();
            nextDirection = ' '; // Clear queued direction
            
            // Update image only when direction actually changes
            updatePacmanImage();
        }
        
        pacman.x += pacman.velocityX;
        pacman.y += pacman.velocityY;

        //check wall collisions
        for (Block wall : walls) {
            if (collision(pacman, wall)) {
                pacman.x -= pacman.velocityX;
                pacman.y -= pacman.velocityY;
                break;
            }
        }

        //check ghost collisions
        for (Ghost ghost : ghosts) {
            if (collision(ghost, pacman)) {
                lives -= 1;
                if (lives == 0) {
                    gameOver = true;
                    return;
                }
                resetPositions();
            }

            ghost.move();  // Chooses direction and sets velocity

            // Tentative move
            ghost.x += ghost.velocityX;
            ghost.y += ghost.velocityY;

            // Check collisions
            boolean collided = false;
            for (Block wall : walls) {
                if (collision(ghost, wall)) {
                    collided = true;
                    break;
                }
            }
            if (ghost.x <= 0 || ghost.x + ghost.width >= boardWidth || collided) {
                // Revert move
                ghost.x -= ghost.velocityX;
                ghost.y -= ghost.velocityY;
                // Re-choose direction
                ghost.move();
            }

        }

        //check food collision
        Block foodEaten = null;
        for (Block food : foods) {
            if (collision(pacman, food)) {
                foodEaten = food;
                score += 10;
            }
        }
        foods.remove(foodEaten);

        if (foods.isEmpty()) {
            loadMap();
            resetPositions();
        }
    }

    public boolean collision(Block a, Block b) {
        return  a.x < b.x + b.width &&
                a.x + a.width > b.x &&
                a.y < b.y + b.height &&
                a.y + a.height > b.y;
    }
    
    // Getter methods for Ghost classes to access game state
    public HashSet<Block> getWalls() {
        return walls;
    }
    
    public Block getPacman() {
        return pacman;
    }
    
    public int getTileSize() {
        return tileSize;
    }
    
    public int getBoardWidth() {
        return boardWidth;
    }
    
    public int getBoardHeight() {
        return boardHeight;
    }

    public void resetPositions() {
        pacman.reset();
        pacman.velocityX = 0;
        pacman.velocityY = 0;
        nextDirection = ' ';
        for (Ghost ghost : ghosts) {
            ghost.reset();
        }
    }
    
    /**
     * Checks if Pac-Man can change direction at the current position.
     * Pac-Man can turn when aligned to the grid and the path in the new direction is clear.
     * 
     * @param newDirection The direction to check
     * @return true if Pac-Man can turn in the new direction, false otherwise
     */
    private boolean canChangeDirection(char newDirection) {
        int tileSize = this.tileSize;
        int gridX = (pacman.x / tileSize) * tileSize;
        int gridY = (pacman.y / tileSize) * tileSize;
        int threshold = tileSize / 4;
        
        boolean alignedX = Math.abs(pacman.x - gridX) < threshold;
        boolean alignedY = Math.abs(pacman.y - gridY) < threshold;
        
        if (newDirection == 'U' || newDirection == 'D') {
            if (!alignedX) return false;
        } else if (newDirection == 'L' || newDirection == 'R') {
            if (!alignedY) return false;
        }
        
        int testX = pacman.x;
        int testY = pacman.y;
        
        testX = gridX;
        testY = gridY;
        
        switch (newDirection) {
            case 'U': testY -= tileSize; break;
            case 'D': testY += tileSize; break;
            case 'L': testX -= tileSize; break;
            case 'R': testX += tileSize; break;
        }
        
        if (testX < 0 || testX >= boardWidth || testY < 0 || testY >= boardHeight) {
            return false;
        }
        
        Block testBlock = new Block(this, null, testX, testY, tileSize, tileSize);
        for (Block wall : walls) {
            if (collision(testBlock, wall)) {
                return false;
            }
        }
        
        return true;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if (gameOver) {
            gameLoop.stop();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {
        if (gameOver) {
            loadMap();
            resetPositions();
            lives = 3;
            score = 0;
            gameOver = false;
            gameLoop.start();
        }
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            nextDirection = 'U';
        }
        else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            nextDirection = 'D';
        }
        else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            nextDirection = 'L';
        }
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            nextDirection = 'R';
        }
    }
    
    /**
     Updates Pac-Man's image based on the current direction.
     This is called only when the direction actually changes, not when queued.
     */
    private void updatePacmanImage() {
        if (pacman.direction == 'U') {
            pacman.image = pacmanUpImage;
        }
        else if (pacman.direction == 'D') {
            pacman.image = pacmanDownImage;
        }
        else if (pacman.direction == 'L') {
            pacman.image = pacmanLeftImage;
        }
        else if (pacman.direction == 'R') {
            pacman.image = pacmanRightImage;
        }
    }
}
