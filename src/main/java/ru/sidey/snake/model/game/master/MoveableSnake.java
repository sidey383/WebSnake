package ru.sidey.snake.model.game.master;

import ru.sidey.snake.model.game.object.Direction;
import ru.sidey.snake.model.game.object.Pose;
import ru.sidey.snake.model.game.object.Snake;
import ru.sidey.snake.model.game.player.Player;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;

public class MoveableSnake implements Snake {

    private final int id;

    private int score;

    private final Deque<Pose> body = new ArrayDeque<Pose>();

    private final Player owner;

    private final Direction direction;

    public MoveableSnake(Snake snake) {
        this.id = snake.id();
        this.score = snake.score();
        this.body.addAll(snake.body());
        this.owner = snake.owner();
        this.direction = snake.direction();
    }

    public void move() {
        body.addFirst(body.getFirst().add(direction));
    }

    public void removeTail() {
        body.removeLast();
    }

    public Pose getHeade() {
        return body.getFirst();
    }

    @Override
    public int id() {
        return id;
    }

    @Override
    public int score() {
        return score;
    }

    public void addScore() {
        score++;
    }

    @Override
    public Collection<Pose> body() {
        return Collections.unmodifiableCollection(body);
    }

    @Override
    public Player owner() {
        return owner;
    }

    @Override
    public Direction direction() {
        return direction;
    }
}
