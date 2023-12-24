package ru.sidey.snake.model.game;

import ru.sidey.snake.SnakesProto;

public enum Direction {
    UP(0, 1), DOWN(0, -1), LEFT(-1, 0), RIGHT(1, 0);

    private final int x;
    private final int y;

    Direction(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Direction opposite() {
        return switch (this) {
            case UP -> DOWN;
            case RIGHT -> LEFT;
            case DOWN -> UP;
            case LEFT -> RIGHT;
        };
    }

    public SnakesProto.Direction toProto() {
        return switch (this) {
            case LEFT -> SnakesProto.Direction.LEFT;
            case DOWN -> SnakesProto.Direction.DOWN;
            case UP -> SnakesProto.Direction.UP;
            case RIGHT -> SnakesProto.Direction.RIGHT;
        };
    }

    public static Direction fromProto(SnakesProto.Direction dir) {
        return switch (dir) {
            case RIGHT -> RIGHT;
            case UP -> UP;
            case DOWN -> DOWN;
            case LEFT -> LEFT;
        };
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
