package ru.sidey.snake.model.game;

import ru.sidey.snake.model.game.player.Player;

import java.util.Collection;

public interface SnakeGame {

    SnakeField getField();

    String name();

    Collection<Player> getPlayer();

    int stateDelay();

}
