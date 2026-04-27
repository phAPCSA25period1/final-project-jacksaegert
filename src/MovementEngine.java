import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class MovementEngine implements KeyListener {
    private volatile boolean listening;
    private boolean rendering;
    private JFrame frame;
    private Grid grid;
    private Player player;

    public MovementEngine() {
        this.listening = false;
    }

    public void startNavigation(Grid grid, Player player) {
        this.grid = grid;
        this.player = player;
        this.listening = true;

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

            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    stopListening();
                }
            });

            frame.addWindowFocusListener(new WindowAdapter() {
                @Override
                public void windowGainedFocus(WindowEvent e) {
                    panel.requestFocusInWindow();
                }
            });
        });

        clearScreen();
        render();

        try {
            while (listening) {
                Thread.sleep(50);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        SwingUtilities.invokeLater(() -> {
            if (frame != null) {
                frame.dispose();
            }
        });
    }

    public void stopListening() {
        listening = false;
    }

    public boolean isListening() {
        return listening;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        boolean moved = false;

        if (rendering) {
            return;
        }

        switch (keyCode) {
            case KeyEvent.VK_W:
                moved = player.moveUp();
                break;
            case KeyEvent.VK_A:
                moved = player.moveLeft();
                break;
            case KeyEvent.VK_S:
                moved = player.moveDown();
                break;
            case KeyEvent.VK_D:
                moved = player.moveRight();
                break;
            case KeyEvent.VK_Q:
                stopListening();
                return;
        }

        if (moved) {
            clearScreen();
            render();
        }
        if(player.checkWin()) {
            stopListening();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

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

    private void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
