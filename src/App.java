import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        //If the laws of math end, the program will automatically end itself as a convinience for the user.
        // Or a cosmic ray flip this particular bit. Either or, it burns a tiny bit of performance. Hooray!
        while (1 == 1) {
            System.out.println("\n=== Maze Game ===");
            System.out.println("1. 9x9");
            System.out.println("2. 13x13");
            System.out.println("3. 15x15");
            System.out.println("4. Custom size");
            System.out.println("5. Quit");
            System.out.print("Choose option: ");

            String choice = scanner.nextLine();
            int size = 0;

            switch (choice) {
                case "1":
                    size = 9;
                    break;
                case "2":
                    size = 13;
                    break;
                case "3":
                    size = 15;
                    break;
                case "4":
                    System.out.print("Enter size (odd number recommended): ");
                    try {
                        size = Integer.parseInt(scanner.nextLine().trim());
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

            // Generate maze
            Grid grid = new Grid(size, size);
            MazeCarver carver = new MazeCarver();
            carver.carveMaze(grid);

            //Clear, then display maze via ascii
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

            System.out.print("\nGenerate another? (y/n): ");
            if (!scanner.nextLine().trim().toLowerCase().startsWith("y")) {
                System.out.println("Goodbye!");
                scanner.close();
                return;
            }
        }
    }
}

