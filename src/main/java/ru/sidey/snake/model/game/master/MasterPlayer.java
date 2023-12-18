package ru.sidey.snake.model.game.master;

import ru.sidey.snake.model.game.object.Snake;
import ru.sidey.snake.model.game.player.Player;
import ru.sidey.snake.model.game.player.PlayerRole;
import ru.sidey.snake.model.game.player.PlayerType;
import ru.sidey.snake.network.ConnectedNode;

public class MasterPlayer implements Player {

    private final int id;
    private final String name;
    private final PlayerType type;
    private final ConnectedNode node;
    private PlayerRole role;
    private Snake snake;

    private int score;

    public MasterPlayer(int id, String name, PlayerType type, ConnectedNode node, PlayerRole role, Snake snake) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.node = node;
        this.role = role;
        this.snake = snake;
    }

    @Override
    public int id() {
        return id;
    }

    @Override
    public synchronized int score() {
        return score;
    }

    public synchronized void snakeScore(int score) {
        if (this.score < score)
            this.score = score;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public synchronized PlayerRole role() {
        return role;
    }

    public synchronized void changeRole(PlayerRole role) {
        this.role = role;
    }

    @Override
    public PlayerType type() {
        return type;
    }

    @Override
    public synchronized Snake snake() {
        return snake;
    }

    public synchronized void removeSnake() {
        this.snake = null;
    }

    public synchronized void setSnake(Snake snake) {
        this.snake = snake;
    }

    public ConnectedNode node() {
        return node;
    }

}
