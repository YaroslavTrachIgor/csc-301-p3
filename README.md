# Pac-Man Game - Custom Ghost AI Implementation

## Original Code Citation

This project is based on an open-source Pac-Man game tutorial:

- **Original Tutorial**: [Pacman Java Tutorial](https://youtu.be/lB_J-VNMVpE)
- **Tutorial Link**: [https://youtu.be/lB_J-VNMVpE](https://youtu.be/lB_J-VNMVpE)
- **Setup Guide**: [How to setup Java with Visual Studio Code](https://youtu.be/BB0gZFpukJU)

The original code demonstrates how to create a Pac-Man game using Java's built-in AWT/Swing graphics library. The tutorial covers:
- Creating the game loop
- Creating a JFrame and JPanel
- Drawing images on the JPanel
- Loading the game map using a tilemap
- Adding keyboard handlers to make Pac-Man move
- Creating a simple algorithm to move each ghost at random
- Detecting collisions between Pac-Man and ghosts
- Having Pac-Man eat food pellets
- Adding a running score
- Resetting the game when Pac-Man collides with a ghost

![pacman-ss](https://github.com/user-attachments/assets/9f16553b-9092-4894-b740-b8903ed24fa9)

## Custom Ghost AI Implementation

This version of the game has been modified for a university programming assignment. The original random ghost movement has been replaced with a custom ghost class structure that allows for individual AI implementations.

### Project Requirements

As specified in the assignment prompt:

> "Clone the open-source Pac-Man game project you see in the current directory. It will be used for the university program in which we need to write a custom code for ghosts:
> 
> - **Blinky**: The direct one. It chases Pac-Man based on current locations.
> - **Pinky**: The smart one. It chases Pac-Man, but it aims for a location 2 dots ahead of Pac-Man, thus trying to anticipate movement.
> - **Inky**: The hybrid. Its movement is a combination of Blinky and Pinky.
> - **Clyde**: The scaredy-cat. It will chase Pac-Man directly, but when it gets within 8 dots it gets scared and runs.
> 
> You should add custom classes for each of those ghosts so that I can program them from scratch. Declare all the basic methods with comments for each ghost class. At the end, you should come up with the same program but the ghosts should not move for now. Pac-Man object should act as it moved before."

### Custom Ghost Classes

The following ghost classes have been created with method stubs and detailed comments:

#### `Ghost.java` (Abstract Base Class)
- Abstract base class for all ghosts
- Extends `Block` to maintain compatibility with existing game mechanics
- Defines common methods: `calculateTarget()`, `chooseDirection()`, and `move()`

#### `Blinky.java` (Red Ghost)
- **Behavior**: Direct chase based on Pac-Man's current location
- **Methods**:
  - `calculateTarget()`: Returns Pac-Man's current position
  - `chooseDirection()`: Chooses direction to move towards target
  - `move()`: Moves Blinky based on direct chase behavior

#### `Pinky.java` (Pink Ghost)
- **Behavior**: Anticipatory chase - aims 2 dots ahead of Pac-Man
- **Methods**:
  - `calculateTarget()`: Returns position 2 dots ahead of Pac-Man
  - `getAnticipatedPosition()`: Calculates anticipated position based on Pac-Man's direction
  - `chooseDirection()`: Chooses direction to move towards anticipated target
  - `move()`: Moves Pinky based on anticipatory behavior

#### `Inky.java` (Blue Ghost)
- **Behavior**: Hybrid of Blinky and Pinky's strategies
- **Methods**:
  - `calculateTarget()`: Returns combined target from both strategies
  - `combineStrategies()`: Combines direct and anticipatory targets
  - `chooseDirection()`: Chooses direction to move towards hybrid target
  - `move()`: Moves Inky based on hybrid behavior

#### `Clyde.java` (Orange Ghost)
- **Behavior**: Scaredy-cat - chases when far, runs when close
- **Methods**:
  - `calculateTarget()`: Returns Pac-Man's position if far, corner if close
  - `getDistanceToPacMan()`: Calculates distance to Pac-Man
  - `isScared()`: Checks if within 8 dots of Pac-Man
  - `chooseDirection()`: Chooses direction based on scared state
  - `move()`: Moves Clyde based on scaredy-cat behavior

### Current Status

- ✅ Ghost classes created with method stubs and detailed comments
- ✅ Ghost movement is currently **disabled** (as per requirements)
- ✅ Pac-Man movement and game mechanics function as before
- ✅ All ghost classes are properly integrated into the game structure

### Implementation Notes

- The `Block` class has been extracted from `PacMan` to allow ghost classes to extend it
- Ghost movement code in `PacMan.move()` has been commented out
- All ghost classes have access to game state through getter methods
- Method implementations are marked with `TODO` comments for future development

### Future Development

To implement ghost AI:
1. Complete the `calculateTarget()` method in each ghost class
2. Implement `chooseDirection()` to select optimal movement direction
3. Implement `move()` to handle actual ghost movement and wall collisions
4. Uncomment ghost movement code in `PacMan.move()`

## Original Homework Suggestions

From the original tutorial:
- Design your own map by modifying the tileMap
- Add power pellets to allow Pac-Man to eat the ghosts
- Fix the opening on left and right where Pac-Man goes off-screen
- Modify ghost movement to cover unreachable areas more effectively
