package ru.sidey.snake.model.game.object;

import ru.sidey.snake.model.game.player.Player;

import java.util.Collection;

public interface Snake {

    int id();

    int score();

    Collection<Pose> body();

    Player owner();

    Direction direction();

}
