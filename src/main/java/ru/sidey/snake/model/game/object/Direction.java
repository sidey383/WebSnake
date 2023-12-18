package ru.sidey.snake.model.game.object;

public enum Direction {
    UP(0, -1), DOWN(0, 1), LEFT(-1, 0), RIGHT(1, 0);
    private final int x;
    private final int y;


    Direction(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

    public Direction opposite() {
        return switch (this) {
            case LEFT -> RIGHT;
            case DOWN -> UP;
            case UP -> DOWN;
            case RIGHT -> LEFT;
        };
    }
}
