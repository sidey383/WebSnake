package ru.sidey.snake.model.game;

import java.util.Collection;

public interface Snake {

    int ownerId();

    Collection<Pose> body();

    Direction direction();

    int score();

}
