package com.example.snakegame;

import java.util.LinkedList;

public class Snake {
    private LinkedList<Segment> body;
    private Direction direction;

    public Snake() {
        this.body = new LinkedList<>();
        this.direction = Direction.RIGHT;
        this.body.add(new Segment(5, 5));  // initial position of snake
    }

    public void move() {
        Segment head = body.get(0);
        Segment newHead = null;

        switch (direction) {
            case UP:
                newHead = new Segment(head.x, head.y - 1);
                break;
            case DOWN:
                newHead = new Segment(head.x, head.y + 1);
                break;
            case LEFT:
                newHead = new Segment(head.x - 1, head.y);
                break;
            case RIGHT:
                newHead = new Segment(head.x + 1, head.y);
                break;
        }

        body.add(0, newHead); // Add new head to the front
        body.remove(body.size() - 1); // Remove tail segment to move
    }


    public void grow() {
        // Add a new segment at the tail to grow the snake
        Segment tail = body.getLast();
        body.addLast(new Segment(tail.x, tail.y));
    }

    public boolean checkCollision() {
        Segment head = body.getFirst();

        // Check if snake hits the wall (out of bounds)
        if (head.x < 0 || head.x >= 20 || head.y < 0 || head.y >= 20) {
            return true;
        }

        // Check if snake collides with itself
        for (int i = 1; i < body.size(); i++) {
            if (body.get(i).x == head.x && body.get(i).y == head.y) {
                return true;
            }
        }

            return false;
        }

        public boolean eatsFood (Food food){
            Segment head = body.getFirst();
            return head.x == food.getX() && head.y == food.getY();
        }

        public void reset () {
            body.clear();
            body.add(new Segment(5, 5));
            direction = Direction.RIGHT;
        }

        public void changeDirection (Direction newDirection){
            // Prevent snake from turning 180 degrees
            if (this.direction == Direction.UP && newDirection != Direction.DOWN) {
                this.direction = newDirection;
            }
            if (this.direction == Direction.DOWN && newDirection != Direction.UP) {
                this.direction = newDirection;
            }
            if (this.direction == Direction.LEFT && newDirection != Direction.RIGHT) {
                this.direction = newDirection;
            }
            if (this.direction == Direction.RIGHT && newDirection != Direction.LEFT) {
                this.direction = newDirection;
            }
        }

        public LinkedList<Segment> getBody () {
            return body;
        }

        public Direction getDirection () {
            return direction;
        }
    }

    enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

    class Segment {
        int x, y;

        public Segment(int x, int y) {
            this.x = x;
            this.y = y;


        }
    }