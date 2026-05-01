import java.util.Scanner;

/**
 * The main entry point for the Maze Game application.
 * <p>
 * This class handles the user menu, maze generation, and game loop.
 * It allows the user to select a maze size, generates a maze using
 * recursive backtracking, and starts the movement engine for gameplay.
 */
public class App {

    /**
     * The main method that runs the Maze Game.
     * <p>
     * Presents a menu for selecting maze dimensions, generates a maze,
     * initializes the player and movement engine, and handles replay logic.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Infinite loop for the game menu; exits via return statements when the user chooses to quit.
        while (true) {
            System.out.println("\n=== Maze Game ===");
            System.out.println("1. 19x19");
            System.out.println("2. 25X25");
            System.out.println("3. 27x27");
            System.out.println("4. Custom size");
            System.out.println("5. Quit");
            System.out.print("Choose option: ");

            String choice = scanner.nextLine();
            int size;

            // Determine maze size based on user menu selection.
            switch (choice) {
                case "1":
                    size = 19;
                    break;
                case "2":
                    size = 25;
                    break;
                case "3":
                    size = 27;
                    break;
                case "4":
                    System.out.print("Enter size (odd number recommended): ");
                    try {
                        size = Integer.parseInt(scanner.nextLine().trim());
                        // Ensure odd dimensions for proper maze generation.
                        if (size % 2 == 0) {
                            System.out.println("Making size odd for proper maze generation.");
                            size++;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid size. Skipping.");
                        continue;
                    }
                    break;
                case "5":
                    System.out.println("Goodbye!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
                    continue;
            }

            // Generate the maze grid and carve paths using DFS.
            Grid grid = new Grid(size, size);
            MazeCarver carver = new MazeCarver();
            carver.carveMaze(grid);

            // Initial display: print the generated maze to the console.
            System.out.println("\033[H\033[2J");
            System.out.println("\nGenerated Maze (" + size + "x" + size + "):");
            for (int r = 0; r < grid.getRows(); r++) {
                for (int c = 0; c < grid.getCols(); c++) {
                    TileType type = grid.getTile(r, c).getTileType();
                    if (type == TileType.PATH) {
                        System.out.print("  ");
                    } else {
                        System.out.print("# ");
                    }
                }
                System.out.println();
            }

            // Initialize the player at the starting position and start the movement engine.
            Player player = new Player(grid, 1, 1);
            MovementEngine engine = new MovementEngine();
            System.out.println("Player at (1,1). Use WASD to move (focus console), Q to quit movement.");
            engine.startNavigation(grid, player);

            // Ask the user if they want to generate another maze.
            System.out.print("\nGenerate another? (y/n): ");
            if (!scanner.nextLine().trim().toLowerCase().startsWith("y")) {
                System.out.println("Goodbye!");
                scanner.close();
                return;
            }
        }
    }
}

