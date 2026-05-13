import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;


/**
 * Handles keyboard input and real-time console rendering for player movement.
 * <p>
 * This class creates a Swing window to capture key events (WASD for movement, Q to quit)
 * and updates the console display to reflect the player's current position in the maze.
 */
public class MovementEngine implements KeyListener {

    /** Flag indicating whether the engine is actively listening for key input. */
    private volatile boolean listening;

    /** Flag to prevent concurrent rendering of the maze. */
    private boolean rendering;

    /** The Swing frame used to capture keyboard focus. */
    private JFrame frame;

    /** Reference to the maze grid. */
    private Grid grid;

    /** Reference to the player whose movement is being controlled. */
    private Player player;

    /** Swing display/renderer for maze + player animation. */
    private Display display;


    /**
     * Constructs a new MovementEngine with listening initially disabled.
     */
    public MovementEngine() {
        this.listening = false;
    }

    /**
     * Starts the navigation loop, opening a Swing window for key input and rendering the maze.
     * <p>
     * This method blocks until the user quits movement (by pressing Q or closing the window).
     *
     * @param grid   the {@link Grid} representing the current maze
     * @param player the {@link Player} object to control
     */
    public void startNavigation(Grid grid, Player player) {
        startNavigation(grid, player, false);
    }

    /**
     * Starts navigation either with Swing (default) or without any GUI when noGui is true.
     *
     * @param grid   the {@link Grid} representing the current maze
     * @param player the {@link Player} object to control
     * @param noGui  when true, no Swing windows are created and movement input is handled via CLI (System.in)
     */
    public void startNavigation(Grid grid, Player player, boolean noGui) {
        this.grid = grid;
        this.player = player;
        this.listening = true;

        if (noGui) {
            runConsoleNavigation();
            return;
        }

        // Create and display a Swing window to capture keyboard input.
        SwingUtilities.invokeLater(() -> {
            frame = new JFrame("Maze Controls");
            frame.setSize(400, 200);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setLocationRelativeTo(null);

            JPanel panel = new JPanel();
            panel.setFocusable(true);
            panel.addKeyListener(this);

            panel.add(new JLabel("Click here, then use WASD to move. Press Q to quit.", SwingConstants.CENTER));

            frame.add(panel);
            frame.setVisible(true);
            panel.requestFocusInWindow();

            // Stop listening when the window is closed.
            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    stopListening();
                }
            });

            // Regain keyboard focus when the window receives focus.
            frame.addWindowFocusListener(new WindowAdapter() {
                @Override
                public void windowGainedFocus(WindowEvent e) {
                    panel.requestFocusInWindow();
                }
            });
        });

        // Create Swing display + initial render.
        display = new Display();
        display.show(grid, player);

        // Initial render (visual only; no console clear needed).
        display.renderNow();

        // Block until the user quits movement.
        try {
            while (listening) {
                Thread.sleep(50);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Dispose of the Swing window on the event dispatch thread.
        SwingUtilities.invokeLater(() -> {
            if (frame != null) {
                frame.dispose();
            }
        });
    }

    /**
     * Console-only navigation loop (no Swing windows, no KeyListeners).
     */
    private void runConsoleNavigation() {
        // Initial render to console.
        clearScreen();
        render();

        java.util.Scanner scanner = new java.util.Scanner(System.in);
        while (listening) {
            System.out.print("\nMove (W/A/S/D) or Q to quit: ");
            String line;
            try {
                line = scanner.nextLine();
            } catch (Exception ex) {
                stopListening();
                break;
            }

            if (line == null || line.isEmpty()) {
                continue;
            }

            char ch = Character.toUpperCase(line.trim().charAt(0));
            int prevRow = player.getRow();
            int prevCol = player.getCol();

            boolean moved;
            switch (ch) {
                case 'W':
                    moved = player.moveUp();
                    break;
                case 'A':
                    moved = player.moveLeft();
                    break;
                case 'S':
                    moved = player.moveDown();
                    break;
                case 'D':
                    moved = player.moveRight();
                    break;
                case 'Q':
                    stopListening();
                    continue;
                default:
                    moved = false;
            }

            if (moved) {
                clearScreen();
                render();
            }

            if (player.checkWin()) {
                System.out.println("Congratulations! You Win!\n");
                stopListening();
            }
        }
    }


    /**
     * Signals the engine to stop listening for keyboard input.
     */
    public void stopListening() {
        listening = false;
    }

    /**
     * Returns whether the engine is currently listening for input.
     *
     * @return true if listening, false otherwise
     */
    public boolean isListening() {
        return listening;
    }

    /**
     * Handles key press events for player movement and quitting.
     * <p>
     * WASD moves the player in the corresponding direction. Q quits movement.
     * The maze is re-rendered to the console after each valid move.
     *
     * @param e the KeyEvent triggered by a key press
     */
    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        boolean moved = false;

        // Ignore input while the maze is being rendered to prevent glitches.
        if (rendering) {
            return;
        }

        // Map key presses to player movement or quit action.
        switch (keyCode) {
            case KeyEvent.VK_W:
                moved = tryMoveAndAnimate(-1, 0);
                break;
            case KeyEvent.VK_A:
                moved = tryMoveAndAnimate(0, -1);
                break;
            case KeyEvent.VK_S:
                moved = tryMoveAndAnimate(1, 0);
                break;
            case KeyEvent.VK_D:
                moved = tryMoveAndAnimate(0, 1);
                break;

            case KeyEvent.VK_Q:
                stopListening();
                return;
        }

        // Check for win condition after each move.

        if (player.checkWin()) {
            System.out.println("Congratulations! You Win! \n");
            stopListening();
        }
    }

    /**
     * Unused key release handler required by the KeyListener interface.
     *
     * @param e the KeyEvent triggered by a key release
     */
    @Override
    public void keyReleased(KeyEvent e) {
    }

    /**
     * Unused key typed handler required by the KeyListener interface.
     *
     * @param e the KeyEvent triggered by a key type
     */
    @Override
    public void keyTyped(KeyEvent e) {
    }

    /**
     * Attempts a move by deltas; animates if move succeeded.
     * <p>
     * @param dRow delta row (up=-1, down=+1)
     * @param dCol delta col (left=-1, right=+1)
     */
    private boolean tryMoveAndAnimate(int dRow, int dCol) {
        int prevRow = player.getRow();
        int prevCol = player.getCol();

        boolean moved;
        if (dRow == 0 && dCol == -1) {
            moved = player.moveLeft();
        } else if (dRow == 0 && dCol == 1) {
            moved = player.moveRight();
        } else if (dRow == -1 && dCol == 0) {
            moved = player.moveUp();
        } else if (dRow == 1 && dCol == 0) {
            moved = player.moveDown();
        } else {
            moved = false;
        }

        if (moved) {
            int toRow = player.getRow();
            int toCol = player.getCol();
            display.animatePlayerMove(prevRow, prevCol, toRow, toCol);
        }

        return moved;
    }

    /**
     * Renders the current state of the maze to the console.

     * <p>
     * The player is displayed as '@', paths as spaces, walls as '#', and the target as '+'.
     */
    private void render() {
        rendering = true;
        for (int r = 0; r < grid.getRows(); r++) {
            for (int c = 0; c < grid.getCols(); c++) {
                if (r == player.getRow() && c == player.getCol()) {
                    System.out.print("@ ");
                } else {
                    TileType type = grid.getTile(r, c).getTileType();
                    if (type == TileType.PATH) {
                        System.out.print("  ");
                    } else if (type == TileType.WALL) {
                        System.out.print("# ");
                    } else {
                        System.out.print("+ ");
                    }
                }
            }
            System.out.println();
        }
        rendering = false;
    }

    /**
     * Clears the console screen using ANSI escape codes.
     */
    private void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}

