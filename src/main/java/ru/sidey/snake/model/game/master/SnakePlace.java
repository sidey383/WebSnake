package ru.sidey.snake.model.game.master;

import ru.sidey.snake.model.game.object.Direction;
import ru.sidey.snake.model.game.object.Pose;

public record SnakePlace(Pose head, Pose tail, Direction direction) {
}
