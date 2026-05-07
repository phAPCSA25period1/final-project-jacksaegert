import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 * Swing display/renderer for the maze and player.
 * <p>
 * Responsibilities:
 * <ul>
 *   <li>Draw the grid (walls/paths/target).</li>
 *   <li>Draw the player.</li>
 *   <li>Animate player movement between cells.</li>
 * </ul>
 */
public class Display {

    private JFrame frame;
    private MazePanel panel;

    /** Size of each maze cell in pixels. */
    private final int cellSize;

    /** Animation duration for a single move. */
    private final int animDurationMs;

    private final Color wallColor = new Color(25, 25, 25);
    private final Color pathColor = new Color(245, 245, 245);
    private final Color targetColor = new Color(64, 160, 255);
    private final Color playerColor = new Color(255, 95, 90);

    private Grid grid;
    private Player player;

    public Display(int cellSize, int animDurationMs) {
        this.cellSize = cellSize;
        this.animDurationMs = animDurationMs;
    }

    /**
     * Convenience constructor with reasonable defaults.
     */
    public Display() {
        this(18, 140);
    }

    /**
     * Creates and shows the Swing window.
     */
    public void show(Grid grid, Player player) {
        this.grid = grid;
        this.player = player;

        SwingUtilities.invokeLater(() -> {
            if (frame != null) {
                frame.dispose();
            }

            panel = new MazePanel();
            panel.setFocusable(false);

            int w = grid.getCols() * cellSize;
            int h = grid.getRows() * cellSize;

            panel.setPreferredSize(new Dimension(w, h));

            frame = new JFrame("Maze");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.getContentPane().add(panel);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    /**
     * Called by the game loop to trigger an animated player move.
     * <p>
     * from/to are grid cell coordinates.
     */
    public void animatePlayerMove(int fromRow, int fromCol, int toRow, int toCol) {
        if (panel == null) return;
        panel.setMoveAnimation(fromRow, fromCol, toRow, toCol);
    }

    /** Draws immediately without animation (useful if you ever need a hard refresh). */
    public void renderNow() {
        if (panel == null) return;
        SwingUtilities.invokeLater(panel::repaint);
    }

    public void close() {
        SwingUtilities.invokeLater(() -> {
            if (frame != null) frame.dispose();
        });
    }

    private class MazePanel extends JPanel {

        private Timer timer;

        // Animation state
        private int animFromRow;
        private int animFromCol;
        private int animToRow;
        private int animToCol;
        private long animStartMs;

        // Progress [0..1]
        private double progress;

        private boolean animating;

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (grid == null) return;

            Graphics2D g2 = (Graphics2D) g.create();
            try {
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

                // Background / grid tiles
                for (int r = 0; r < grid.getRows(); r++) {
                    for (int c = 0; c < grid.getCols(); c++) {
                        paintTile(g2, r, c);
                    }
                }

                // Draw player (either interpolated or at current cell)
                int playerRow;
                int playerCol;
                if (animating) {
                    // Interpolate pixel position
                    double fromX = animFromCol * cellSize + cellSize / 2.0;
                    double fromY = animFromRow * cellSize + cellSize / 2.0;
                    double toX = animToCol * cellSize + cellSize / 2.0;
                    double toY = animToRow * cellSize + cellSize / 2.0;

                    double x = fromX + (toX - fromX) * progress;
                    double y = fromY + (toY - fromY) * progress;

                    paintPlayerAtPixel(g2, x, y);
                    return;
                } else {
                    playerRow = player.getRow();
                    playerCol = player.getCol();
                }

                double x = playerCol * cellSize + cellSize / 2.0;
                double y = playerRow * cellSize + cellSize / 2.0;
                paintPlayerAtPixel(g2, x, y);

            } finally {
                g2.dispose();
            }
        }

        private void paintTile(Graphics2D g2, int r, int c) {
            TileType type = grid.getTile(r, c).getTileType();

            int x = c * cellSize;
            int y = r * cellSize;

            switch (type) {
                case WALL:
                    g2.setColor(wallColor);
                    break;
                case PATH:
                    g2.setColor(pathColor);
                    break;
                case TARGET:
                    g2.setColor(pathColor);
                    break;
                default:
                    g2.setColor(pathColor);
            }

            g2.fillRect(x, y, cellSize, cellSize);

            // Slight outline for readability
            g2.setColor(new Color(210, 210, 210));
            ((Graphics2D) g2).setStroke(new BasicStroke(1f));
            g2.drawRect(x, y, cellSize, cellSize);

            if (type == TileType.TARGET) {
                g2.setColor(targetColor);
                int pad = Math.max(3, cellSize / 6);
                g2.fillOval(x + pad, y + pad, cellSize - pad * 2, cellSize - pad * 2);
            }
        }

        private void paintPlayerAtPixel(Graphics2D g2, double centerX, double centerY) {
            int radius = Math.max(4, cellSize / 3);

            // subtle shadow
            g2.setColor(new Color(0, 0, 0, 35));
            g2.fillOval((int) centerX - radius, (int) centerY - radius + 2, radius * 2, radius * 2);

            g2.setColor(playerColor);
            g2.fillOval((int) centerX - radius, (int) centerY - radius, radius * 2, radius * 2);

            // highlight
            g2.setColor(new Color(255, 255, 255, 160));
            int hx = (int) centerX - radius / 2;
            int hy = (int) centerY - radius / 2;
            g2.fillOval(hx, hy, radius, radius);
        }

        void setMoveAnimation(int fromRow, int fromCol, int toRow, int toCol) {
            if (fromRow == toRow && fromCol == toCol) return;

            this.animFromRow = fromRow;
            this.animFromCol = fromCol;
            this.animToRow = toRow;
            this.animToCol = toCol;

            this.progress = 0.0;
            this.animStartMs = System.currentTimeMillis();
            this.animating = true;

            int delay = 15; // ~60fps
            if (timer != null && timer.isRunning()) timer.stop();

            timer = new Timer(delay, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    long now = System.currentTimeMillis();
                    long elapsed = now - animStartMs;
                    progress = Math.min(1.0, elapsed / (double) animDurationMs);

                    // Repaint with updated progress
                    repaint();

                    if (progress >= 1.0) {
                        animating = false;
                        repaint();
                        timer.stop();
                    }
                }
            });
            timer.start();
        }
    }
}

