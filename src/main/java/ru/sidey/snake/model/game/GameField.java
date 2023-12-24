package ru.sidey.snake.model.game;

import java.util.Collection;

public interface GameField {

    Collection<? extends Snake> snakes();

    Collection<Pose> food();

    FieldConfig config();

}
