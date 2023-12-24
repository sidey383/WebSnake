package ru.sidey.snake.model.game.master;

import ru.sidey.snake.model.game.Direction;
import ru.sidey.snake.model.game.Pose;
import ru.sidey.snake.model.game.Snake;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;

public class MasterSnake implements Snake {

    private final int id;

    private final Deque<Pose> poses = new ArrayDeque<>();

    private Direction direction;

    private int score;

    public MasterSnake(int id, Pose head, Direction direction) {
        this.id = id;
        poses.add(head);
        poses.addLast(head.applyDirection(direction.opposite()));
    }

    @Override
    public int ownerId() {
        return id;
    }

    @Override
    public Collection<Pose> body() {
        return poses;
    }

    public Pose head() {
        return poses.getFirst();
    }

    @Override
    public Direction direction() {
        return direction;
    }

    @Override
    public int score() {
        return score;
    }

    public void incrementScore() {
        this.score++;
    }

    public void moveBody() {
        poses.addFirst(poses.getFirst().applyDirection(direction));
    }

    public void removeTail() {
        poses.removeLast();
    }

    public void changeDirection(Direction direction) {
        this.direction = direction;
    }

}
