package ru.sidey.snake.model.game.object;

import ru.sidey.snake.model.game.player.Player;

import java.util.Collection;

public class SimpleSnake implements Snake {

    private final int id;

    private int score;

    private final Collection<Pose> body;

    private final Player owner;

    private final Direction direction;

    public SimpleSnake(int id, Collection<Pose> body, Player owner, Direction direction) {
        this.id = id;
        this.body = body;
        this.owner = owner;
        this.direction = direction;
    }

    public SimpleSnake(Snake snake, Player owner) {
        this.id = snake.id();
        this.body = snake.body();
        this.owner = owner;
        this.direction = snake.direction();
        this.score = snake.score();
    }

    @Override
    public int id() {
        return id;
    }

    @Override
    public int score() {
        return score;
    }

    @Override
    public Collection<Pose> body() {
        return body;
    }

    @Override
    public Player owner() {
        return owner;
    }

    @Override
    public Direction direction() {
        return direction;
    }

    public SimpleSnake removeOwner() {
        return new SimpleSnake(id, body, null, direction);
    }

}
