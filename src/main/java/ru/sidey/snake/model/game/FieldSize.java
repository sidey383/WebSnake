package ru.sidey.snake.model.game;

public record FieldSize(int width, int height) {
    public int total() {
        return width * height;
    }
}
