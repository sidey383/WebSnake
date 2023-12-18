package ru.sidey.snake.model.game.player;

import ru.sidey.snake.model.game.object.Snake;

public interface Player {

    int id();

    int score();

    String name();

    PlayerRole role();

    PlayerType type();

    Snake snake();

}
