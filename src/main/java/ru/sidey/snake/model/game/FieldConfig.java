package ru.sidey.snake.model.game;

public record FieldConfig(
        int width,
        int height,
        int foodStatic,
        int stateDelayMs
) implements Size {
}
