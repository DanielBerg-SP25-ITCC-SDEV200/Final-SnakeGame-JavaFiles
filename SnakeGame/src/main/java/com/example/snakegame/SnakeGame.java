package com.example.snakegame;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class SnakeGame {
    private Snake snake;
    private Food food;
    private int score;
    private boolean gameOver;
    private boolean paused;
    private boolean gameStarted;
    private AnimationTimer gameLoop;
    private GraphicsContext gc;

    // Game speed (in nanoseconds, adjust this as needed)
    private long gameSpeed = 95000000L;  // 95ms for one game loop update (adjust as necessary)

    public SnakeGame(GraphicsContext gc) {
        this.snake = new Snake();
        this.food = new Food(this.snake);
        this.score = 0;
        this.gameOver = false;
        this.paused = false;
        this.gameStarted = false;
        this.gc = gc;
    }

    // Reset the game state
    public void restartGame() {
        // Reset the snake
        this.snake = new Snake();  // Resets the snake to the initial state

        // Reset the food
        this.food = new Food(this.snake);  // Generates new food

        // Reset score
        this.score = 0;

        // Reset game-over state
        this.gameOver = false;

        // Reset paused state
        this.paused = false;

        // Reset game-started state
        this.gameStarted = true;

        // Reset the game speed to the initial value
        this.gameSpeed = 95000000L;  // Reset to the initial speed (adjust as needed)

        if (gameLoop != null) {
            gameLoop.stop(); // Stop any previously running loop
        }
        // Recreate the game loop and start it
        long[] lastUpdateTime = {System.nanoTime()};  // Initial timestamp for the loop

        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (paused || gameOver) {
                    return; // Skip if paused or game over
                }

                if (now - lastUpdateTime[0] >= gameSpeed) {
                    update();
                    draw();
                    lastUpdateTime[0] = now;  // Update last update time
                }
            }
        };
        gameLoop.start();  // Start the new game loop
    }

    // Start a new game
    public void startGame() {
        this.snake.reset();
        this.food.generateNewFood(this.snake);
        this.score = 0;
        this.gameOver = false;
        this.paused = false;
        this.gameStarted = true;

        long[] lastUpdateTime = {0};
        this.gameSpeed = 95000000L;

        if (gameLoop != null) {
            gameLoop.stop(); // Stop any previously running loop
        }

        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (paused || gameOver) {
                    return; // Do nothing if paused or game over

                }
                if (now - lastUpdateTime[0] >= gameSpeed) {
                    update();
                    draw();
                    lastUpdateTime[0] = now;
                }
            }
        };
        gameLoop.start();
    }

    // Pause the game
    public void pauseGame() {
        this.paused = true;
        if (gameLoop != null) {
            gameLoop.stop();  // Stop the game loop when paused
        }
    }

    // Resume the game
    public void resumeGame() {
        this.paused = false;

        // Restart the game loop if it's not already running
        if (gameLoop != null) {
            long[] lastUpdateTime = {System.nanoTime()};  // Timestamp for game loop


            gameLoop = new AnimationTimer() {
                @Override
                public void handle(long now) {
                    if (paused || gameOver) {
                        return; // Do nothing if paused or game over
                    }

                    // Update game logic at regular intervals
                    if (now - lastUpdateTime[0] >= gameSpeed) {
                        update();
                        draw();
                        lastUpdateTime[0] = now;
                    }
                }
            };
        }
        gameLoop.start(); // Start or resume the game loop
    }

    // End the game
    public void endGame() {
        this.gameOver = true;
        if (gameLoop != null) {
            gameLoop.stop();
        }

    }

    // Update game logic: move snake, check collisions, generate food
    public void update() {
        if (gameOver) {
            return;
        }

        this.snake.move();
        if (this.snake.checkCollision()) {
            endGame();
        }
        if (this.snake.eatsFood(food)) {
            this.score++;
            this.snake.grow();
            food.generateNewFood(this.snake);
        }
    }

    // Draw the game to the canvas
    public void draw() {
        // Use the GraphicsContext to draw the snake, food, and score
        gc.clearRect(0, 0, 400, 400);  // Clear previous frame


        // Draw the border
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(5);
        gc.strokeRect(0, 0, 400, 400);

        // Draw the snake
        gc.setFill(Color.GREEN);
        for (Segment segment : snake.getBody()) {
            gc.fillRect(segment.x * 20, segment.y * 20, 20, 20);
        }

        // Draw the food
        gc.setFill(Color.RED);
        gc.fillRect(food.getX() * 20, food.getY() * 20, 20, 20);

        // Draw score
        gc.setFill(Color.BLACK);
        gc.setFont(javafx.scene.text.Font.font(15));
        gc.fillText("Score: " + score, 10, 30);

        // Draw "Game Over" message
        if (gameOver) {
            gc.setFill(Color.BLACK);
            gc.setFont(javafx.scene.text.Font.font(30));
            gc.fillText("Game Over!", 150, 200);
        }
    }

    public int getScore() {
        return score;
    }

    public Snake getSnake() {
        return snake;
    }

    public Food getFood() {
        return food;
    }

    public boolean isPaused() {
        return paused;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public boolean isGameStarted() {
        return gameStarted;
    }
}
