# The Pac-Man Game

<div align="center">

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Java Version](https://img.shields.io/badge/Java-8%2B-orange?style=for-the-badge)
![License](https://img.shields.io/badge/License-Academic-blue?style=for-the-badge)
![Status](https://img.shields.io/badge/Status-In%20Development-yellow?style=for-the-badge)

**University of Tampa | CSC 301-1 Fall 2025**  
**Developed by:** Yaroslav Trach and Darwin Prowant

[![GitHub](https://img.shields.io/badge/GitHub-Repository-black?style=flat-square&logo=github)](https://github.com/YaroslavTrachIgor/csc-301-p3)

</div>

---

## Table of Contents

- [Project Overview](#-project-overview)
- [Prerequisites](#-prerequisites)
- [Installation](#-installation)
- [Building and Running](#-building-and-running)
- [Project Structure](#-project-structure)
- [Academic Citation](#-academic-citation)

---

## Project Overview

This project is part of the **CSC 301-1 (Fall 2025)** course at the University of Tampa. The assignment involves implementing custom artificial intelligence for the four ghosts in a Pac-Man game, each with distinct behavioral patterns.

The game is built using Java's AWT/Swing graphics library and features:
- Custom ghost AI classes with method stubs for implementation
- Tilemap-based level system
- Collision detection
- Score tracking
- Game state management

![pacman-ss](https://github.com/user-attachments/assets/9f16553b-9092-4894-b740-b8903ed24fa9)

---

## Prerequisites

Before installing and running this project, ensure you have the following installed:

- **Java Development Kit (JDK) 8 or higher**
  ```bash
  java -version
  javac -version
  ```
  
- **Git** (for cloning the repository)
  ```bash
  git --version
  ```

- **IDE** (optional but recommended):
  - IntelliJ IDEA
  - Eclipse
  - Visual Studio Code with Java extensions

---

## Installation

### Step 1: Clone the Repository

```bash
git clone https://github.com/YaroslavTrachIgor/csc-301-p3.git
cd csc-301-p3
```

### Step 2: Verify Project Structure

Ensure the following files are present in the project directory:

```bash
ls -la
```

Expected files:
```
App.java
PacMan.java
Block.java
Ghost.java
Blinky.java
Pinky.java
Inky.java
Clyde.java
*.png (image files)
```

### Step 3: Verify Image Resources

The project requires the following image files in the root directory:
- `wall.png`
- `pacmanUp.png`, `pacmanDown.png`, `pacmanLeft.png`, `pacmanRight.png`
- `redGhost.png`, `pinkGhost.png`, `blueGhost.png`, `orangeGhost.png`
- `scaredGhost.png`, `powerFood.png`, `cherry.png`, `cherry2.png`

---

## Building and Running

### Method 1: Command Line Compilation

#### Compile the Project

```bash
# Compile all Java files
javac -d out *.java

# Or compile to current directory
javac *.java
```

#### Run the Application

```bash
# If compiled to 'out' directory
java -cp out App

# If compiled to current directory
java App
```

### Method 2: Using an IDE

#### IntelliJ IDEA
1. Open IntelliJ IDEA
2. Select `File` → `Open`
3. Navigate to the project directory and select it
4. Wait for the project to sync
5. Right-click on `App.java` → `Run 'App.main()'`

#### Visual Studio Code
1. Open VS Code in the project directory
2. Install the "Extension Pack for Java" if not already installed
3. Open `App.java`
4. Click the "Run" button above the `main` method
5. Or use the terminal: `java App`

#### Eclipse
1. Open Eclipse
2. Select `File` → `Import` → `Existing Projects into Workspace`
3. Browse to the project directory
4. Select the project and click `Finish`
5. Right-click on `App.java` → `Run As` → `Java Application`

### Expected Output

Upon successful execution, a game window should appear with:
- Pac-Man character controllable via arrow keys
- Ghosts positioned on the map (currently stationary)
- Food pellets to collect
- Score and lives display

---

## Project Structure

```
csc-301-p3/
│
├── App.java                 # Main entry point
├── PacMan.java              # Main game class (JPanel, game loop, rendering)
├── Block.java               # Base class for game entities
├── Ghost.java               # Abstract base class for ghosts
│
├── Blinky.java              # Red ghost - direct chase behavior
├── Pinky.java               # Pink ghost - anticipatory chase
├── Inky.java                # Blue ghost - hybrid behavior
├── Clyde.java               # Orange ghost - scaredy-cat behavior
│
├── *.png                    # Image resources (sprites, walls, etc.)
├── README.md                # This file
└── .gitignore               # Git ignore rules
```

### Key Classes

#### `App.java`
Main application entry point that creates the JFrame and initializes the game.

```java
public class App {
    public static void main(String[] args) throws Exception {
        JFrame frame = new JFrame("Pac Man");
        PacMan pacmanGame = new PacMan();
        frame.add(pacmanGame);
        frame.pack();
        frame.setVisible(true);
    }
}
```

#### `Ghost.java` (Abstract Base Class)
Defines the interface for all ghost implementations.

```java
public abstract class Ghost extends Block {
    public abstract Point calculateTarget();
    public abstract char chooseDirection();
    public abstract void move();
}
```

#### Ghost Implementations
Each ghost class extends `Ghost` and implements unique AI behavior:
- **Blinky**: Direct pathfinding to Pac-Man's current position
- **Pinky**: Targets position 2 dots ahead of Pac-Man
- **Inky**: Combines Blinky and Pinky strategies
- **Clyde**: Chases when far, flees when within 8 dots

---

## Summary

This section describes the implementation approach for each ghost's behavior.

Blinky uses BFS to find the shortest path to Pac-Man's current position. BFS is ideal for this direct pursuit strategy because it guarantees the shortest path in terms of number of steps, exploring all nodes at the current depth level before moving to the next level. The algorithm uses a queue (FIFO) data structure to ensure nodes are processed in the order they were discovered, creating a level-by-level expansion from Blinky's position to Pac-Man's current location.

Pinky uses the A* pathfinding algorithm to target a position 2 dots (tiles) ahead of Pac-Man in the direction Pac-Man is currently moving. This anticipatory behavior attempts to intercept Pac-Man by predicting where they will be. A* is chosen for this strategy because it efficiently finds optimal paths using heuristic guidance (Euclidean distance), making it more efficient than BFS while still guaranteeing optimal solutions.

Inky implements a hybrid strategy that combines both Blinky's direct chase and Pinky's anticipatory behavior. Inky calculates a combined target by averaging Blinky's target (Pac-Man's current position) and Pinky's target (2 dots ahead of Pac-Man). The ghost then runs both pathfinding algorithms simultaneously: BFS is executed to find the path to Blinky's target, while A* is executed to find the path to Pinky's target. The algorithm chooses between BFS and A* results using a priority system: if both algorithms agree on the same direction and that direction is valid, Inky uses that direction for high confidence. If they disagree, Inky prefers the A* direction if it's valid (since A* is more efficient and heuristic-guided), otherwise falls back to the BFS direction if valid. If both are invalid, a fallback method finds any valid direction. Finally, we can say that this hybrid approach makes Inky unpredictable yet strategic since it considers both immediate target and future positioning.

Clyde implements a distance-based behavioral switch between chasing and scared modes using the A* pathfinding algorithm. Each frame, Clyde calculates the Euclidean distance to Pac-Man using the formula `√((pacman.x - clyde.x)² + (pacman.y - clyde.y)²)`. The scared distance threshold is defined as 8 dots (tiles), calculated as `tileSize * 8` pixels. When the distance is greater than 8 dots, Clyde enters chase mode, targeting Pac-Man's current position and using A* to chase directly. When the distance is 8 dots or less, Clyde becomes scared and enters new mode, targeting the farthest corner from Pac-Man also using A* search. The algorithm evaluates all four corners of the board and selects the one with the maximum distance from Pac-Man's current position.

---

## Academic Citation

### Original Source Code

YouTube. (n.d.). *Pacman Java Tutorial* [Video]. YouTube. https://youtu.be/lB_J-VNMVpE

### Research Reference

Novikov, A., Yakovlev, S., & Gushchin, I. (2025). Exploring the possibilities of MADDPG for UAV swarm control by simulating in Pac-Man environment. *Radioelectronic and Computer Systems*, *1*(113), 21. https://doi.org/10.32620/reks.2025.1.21

Obregon, A. (2025). Pathfinding with the A-star algorithm in Java. Medium. https://medium.com/@AlexanderObregon/pathfinding-with-the-a-star-algorithm-in-java-3a66446a2352

### Assignment Requirements

Cursor AI. (2025). *Code generation and implementation assistance* [AI software]. Cursor. https://cursor.sh
> "Clone the open-source Pac-Man game project you see in the current directory. It will be used for the university program in which we need to write a custom code for ghosts:
> 
> - **Blinky**: The direct one. It chases Pac-Man based on current locations.
> - **Pinky**: The smart one. It chases Pac-Man, but it aims for a location 2 dots ahead of Pac-Man, thus trying to anticipate movement.
> - **Inky**: The hybrid. Its movement is a combination of Blinky and Pinky.
> - **Clyde**: The scaredy-cat. It will chase Pac-Man directly, but when it gets within 8 dots it gets scared and runs.
> 
> You should add custom classes for each of those ghosts so that I can program them from scratch. Declare all the basic methods with comments for each ghost class. At the end, you should come up with the same program but the ghosts should not move for now. Pac-Man object should act as it moved before."

---

## 🏷️ Tags

`java` `pacman` `game-development` `artificial-intelligence` `awt` `swing` `university-project` `csc-301` `university-of-tampa` `ghost-ai` `pathfinding` `game-ai`

---

<div align="center">

**University of Tampa | CSC 301-1 Fall 2025**

[![GitHub](https://img.shields.io/badge/GitHub-Repository-black?style=flat-square&logo=github)](https://github.com/YaroslavTrachIgor/csc-301-p3)

</div>
