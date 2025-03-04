package com.example.snakegame;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

public class Main extends Application {
    private SnakeGame game;
    private Canvas canvas;
    private StackPane root;
    private VBox controlButtons;
    private Rectangle border;
    private Button startButton;
    private Button pauseButton;
    private Button resetButton;
    private Button resumeButton;

    private static final int INITIAL_GAME_SPEED = 95000000; // 95 ms in nanoseconds
    private int currentGameSpeed = INITIAL_GAME_SPEED;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Initialize canvas and GraphicsContext
        canvas = new Canvas(400, 400);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        game = new SnakeGame(gc);

        // Create the border around the canvas
        border = new Rectangle(0, 0, 400, 400);  // position and size of the border
        border.setStroke(Color.BLACK);  // Set the border color to black
        border.setFill(Color.TRANSPARENT);  // Make the inside transparent
        border.setStrokeWidth(5);  // Set the thickness of the border

        // Create the control buttons (Start, Pause, Reset)
        controlButtons = createControlButtons();

        // Create the layout and add canvas and control buttons
        root = new StackPane();
        root.getChildren().addAll(canvas, border);
        root.getChildren().add(controlButtons);

        Scene scene = new Scene(root, 400, 450);

        // Ensure the root container captures key input by focusing on it
        scene.setOnKeyPressed(event -> handleKeyInput(event.getCode()));

        // Focus the root layout so it can receive keyboard input immediately
        root.setFocusTraversable(true);  // Focus the VBox root layout

        primaryStage.setTitle("Snake Game");
        primaryStage.setScene(scene);
        primaryStage.show();

    }


    // Handle user input (arrow keys or WASD keys)
    private void handleKeyInput(KeyCode keyCode) {
        if (keyCode == KeyCode.UP && game.getSnake().getDirection() != Direction.DOWN) {
            game.getSnake().changeDirection(Direction.UP);
        } else if (keyCode == KeyCode.DOWN && game.getSnake().getDirection() != Direction.UP) {
            game.getSnake().changeDirection(Direction.DOWN);
        } else if (keyCode == KeyCode.LEFT && game.getSnake().getDirection() != Direction.RIGHT) {
            game.getSnake().changeDirection(Direction.LEFT);
        } else if (keyCode == KeyCode.RIGHT && game.getSnake().getDirection() != Direction.LEFT) {
            game.getSnake().changeDirection(Direction.RIGHT);
        }
    }

    // Start the game by hiding the buttons
    private void startGame() {
        // Ensure buttons are not null before adding them to the layout
        game.startGame();
        root.requestFocus();
        startButton.setVisible(false);

        // Show Pause and Restart buttons, hide Resume button initially
        pauseButton.setVisible(true);
        resetButton.setVisible(true);



        // Focus the root layout again to ensure key events are captured
        root.requestFocus();


        // Reset the game speed to the initial value (this is where the fix happens)
        resetGameSpeed();

        // Start the game logic
        game.startGame();
    }

    private void resetGameSpeed() {
        currentGameSpeed = INITIAL_GAME_SPEED;
    }

    // Create control buttons (Start, Pause, Reset)
    private VBox createControlButtons() {
        Button startButton = new Button("Start Game");
        startButton.setOnAction(event -> {
            game.startGame();
            root.requestFocus();
            startButton.setVisible(false);
            pauseButton.setVisible(true);
            resetButton.setVisible(true);
            resumeButton.setVisible(true);
        });



        Button pauseButton = new Button("Pause Game");
        pauseButton.setOnAction(event -> {
            game.pauseGame();
            pauseButton.setVisible(true);
            resumeButton.setVisible(true);

        });

        resumeButton = new Button("Resume Game");
        resumeButton.setOnAction(event -> {
            game.resumeGame();
            resetGameSpeed();  // Reset the game speed to the initial value when resumed.

            // Ensure the root layout gets focus again to capture key events
            root.requestFocus();

            resumeButton.setVisible(true);
            pauseButton.setVisible(true);
        });

        Button resetButton = new Button("Restart Game");
        resetButton.setOnAction(event -> restartGame());

        // Initially, set pause and reset buttons to be invisible
        pauseButton.setVisible(true);
        resetButton.setVisible(true);
        resumeButton.setVisible(true);

        VBox buttons = new VBox(10, startButton, pauseButton, resumeButton, resetButton);
        return buttons;
    }



    // Restart the game by showing the buttons again (optional)
    private void restartGame() {
        // Reset the game state (without clearing the entire screen)
        game.restartGame();

        // Reset the snake speed to initial speed
        resetGameSpeed();

        root.requestFocus();

        // Clear previous game and buttons
        root.getChildren().clear();
        root.getChildren().addAll(canvas, border, controlButtons);

        // Show the Restart and Pause buttons
        resetButton.setVisible(true);
        pauseButton.setVisible(true);
        resumeButton.setVisible(false);  // Hide the resume button

        // Redraw the game with reset values
        game.draw();
        game.restartGame();


        }
    }


