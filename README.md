# Micromaze

Micromaze is a lightweight Java maze-generation and navigation project built around procedural maze carving, grid-based movement, and a simple rendering system.

The project focuses on clean object-oriented structure and separation of responsibilities: maze generation, rendering, movement, and grid management are all handled independently.

---

## Features

* Procedurally generated mazes
* Recursive maze carving system
* Randomized extra passages for less predictable layouts
* Grid-based player movement
* Keyboard navigation
* Swing-based rendering and animation
* Modular architecture for easy expansion

---

## Project Structure

### Core Classes

| Class            | Responsibility                                           |
| ---------------- | -------------------------------------------------------- |
| `App`            | Entry point of the application                           |
| `Grid`           | Stores and manages the maze tiles                        |
| `Tile`           | Represents a single cell in the maze                     |
| `TileType`       | Enum describing tile behavior (`WALL`, `PATH`, `TARGET`) |
| `MazeCarver`     | Generates the maze layout                                |
| `Player`         | Handles player position and movement validation          |
| `MovementEngine` | Processes keyboard input and movement logic              |
| `Display`        | Renders the maze and player to the screen                |

---

## How It Works

### Maze Generation

The `MazeCarver` class builds the maze recursively using directional carving and weighted continuation logic. The generator also adds occasional extra passages to avoid overly linear paths.

Once the maze is complete, the algorithm finds a distant endpoint and marks it as the target tile.

### Player Navigation

The `Player` class validates movement against the grid, preventing movement into walls while allowing traversal across valid paths.

The `MovementEngine` listens for keyboard input and coordinates movement animations through the `Display` system.

### Rendering

Rendering is handled using Java Swing. The `Display` class manages:

* Drawing the maze
* Rendering the player
* Movement animations
* Window lifecycle

---

## Controls

| Key       | Action     |
| --------- | ---------- |
| `W` / `↑` | Move Up    |
| `S` / `↓` | Move Down  |
| `A` / `←` | Move Left  |
| `D` / `→` | Move Right |

---

## Running the Project

### Requirements

* Java 17+ (recommended)
* Any Java IDE or command-line compiler

### Compile

```bash
javac *.java
```

### Run

```bash
java App
```

---

## Design Goals

Micromaze was built with a few core goals in mind:

* Keep the codebase readable and modular
* Separate gameplay systems cleanly
* Make maze-generation logic easy to tweak and experiment with
* Provide a foundation for future features

---

## Possible Future Improvements

Some ideas that could be added later:

* Difficulty settings
* Timed runs
* Enemy AI
* Fog-of-war rendering
* Multiple maze-generation algorithms
* Saving/loading mazes
* Sound effects and music
* Better UI and menus
* Procedural themes or tile sets

---

## UML Overview

The project architecture follows a relatively straightforward dependency flow:

```text
App
 ├── Grid
 ├── MazeCarver
 ├── Player
 └── MovementEngine
        └── Display
```

The grid acts as the shared world state while movement, rendering, and generation remain decoupled.

---

## License

This project is open for educational and personal use.

---

## Notes

Micromaze is intentionally small in scope. The goal is not to be a full game engine, but a compact and extensible project for experimenting with procedural generation, rendering systems, and game architecture in Java.
