import java.awt.*;
import java.util.HashSet;

/*
Yaroslav Trach and Darwin Prowant
CSC 301
Program 3
Last Edited: 12/04/2025

This class is the main Block object. It is used for walls, food, Pac-Man, and ghosts.

ACADEMIC CITATION:
YouTube. (n.d.). *Pacman Java Tutorial* [Video]. YouTube. https://youtu.be/lB_J-VNMVpE
*/
public class Block {
    int x;
    int y;
    int width;
    int height;
    Image image;

    int startX;
    int startY;
    char direction = 'U'; // U D L R
    int velocityX = 0;
    int velocityY = 0;
    
    // Reference to game for accessing walls and collision method
    protected PacMan game;
    protected int tileSize;

    Block(PacMan game, Image image, int x, int y, int width, int height) {
        this.game = game;
        this.image = image;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.startX = x;
        this.startY = y;
        if (game != null) {
            this.tileSize = game.getTileSize();
        }
    }

    void updateDirection(char direction) {
        char prevDirection = this.direction;
        this.direction = direction;
        updateVelocity();
        this.x += this.velocityX;
        this.y += this.velocityY;
        if (game != null && game.getWalls() != null) {
            for (Block wall : game.getWalls()) {
                if (game.collision(this, wall)) {
                    this.x -= this.velocityX;
                    this.y -= this.velocityY;
                    this.direction = prevDirection;
                    updateVelocity();
                }
            }
        }
    }

    void updateVelocity() {
        if (this.direction == 'U') {
            this.velocityX = 0;
            this.velocityY = -tileSize/4;
        }
        else if (this.direction == 'D') {
            this.velocityX = 0;
            this.velocityY = tileSize/4;
        }
        else if (this.direction == 'L') {
            this.velocityX = -tileSize/4;
            this.velocityY = 0;
        }
        else if (this.direction == 'R') {
            this.velocityX = tileSize/4;
            this.velocityY = 0;
        }
    }

    void reset() {
        this.x = this.startX;
        this.y = this.startY;
    }
}

