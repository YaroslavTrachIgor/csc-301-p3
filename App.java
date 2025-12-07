import javax.swing.JFrame;

/*
Yaroslav Trach and Darwin Prowant
CSC 301
Program 3
Last Edited: 12/04/2025

This class is the main starting point of the program. It is responsible for creating the setting up the JFrame and running the game.

ACADEMIC CITATION:
YouTube. (n.d.). *Pacman Java Tutorial* [Video]. YouTube. https://youtu.be/lB_J-VNMVpE
 */
public class App {

    // MARK: - Lifecycle
    public static void main(String[] args) throws Exception {
        int rowCount = 21;
        int columnCount = 19;
        int tileSize = 32;
        int boardWidth = columnCount * tileSize;
        int boardHeight = rowCount * tileSize;

        JFrame frame = new JFrame("Pac Man");
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        PacMan pacmanGame = new PacMan();
        frame.add(pacmanGame);
        frame.pack();
        pacmanGame.requestFocus();
        frame.setVisible(true);
    }
}
