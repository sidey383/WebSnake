package ru.sidey.snake.model.game;

import ru.sidey.snake.model.game.object.Pose;
import ru.sidey.snake.model.game.object.Snake;

import java.util.Collection;

public interface SnakeField {

    int width();

    int height();

    int staticFood();

    Collection<Snake> getSnakes();

    Collection<Pose> getFood();

    int order();

    FieldSize size();

}
