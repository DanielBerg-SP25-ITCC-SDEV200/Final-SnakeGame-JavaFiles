package com.example.snakegame;

import java.util.Random;

public class Food {
    private int x, y;

    public Food(Snake snake) {
        generateNewFood(snake);  // Pass the snake to generate food
    }

    public void generateNewFood(Snake snake) {
        Random rand = new Random();
        // Ensure food doesn't spawn on the snake's body
        do {
            this.x = rand.nextInt(20);
            this.y = rand.nextInt(20);
        } while (isFoodOnSnake(snake));  // Check if food is on the snake
    }

    private boolean isFoodOnSnake(Snake snake) {
        for (Segment segment : snake.getBody()) {
            if (segment.x == this.x && segment.y == this.y) {
                return true;  // Food is on the snake
            }
        }
        return false;  // Food is not on the snake
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}

