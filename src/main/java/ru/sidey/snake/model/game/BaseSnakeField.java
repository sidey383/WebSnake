package ru.sidey.snake.model.game;

import ru.sidey.snake.model.game.object.Pose;
import ru.sidey.snake.model.game.object.Snake;

import java.util.Collection;

public class BaseSnakeField implements SnakeField {

    protected final int order;

    protected final FieldSize size;

    protected final int staticFood;

    protected final Collection<Snake> snakes;

    protected final Collection<Pose> food;

    public BaseSnakeField(int order, FieldSize size, int staticFood, Collection<Snake> snakes, Collection<Pose> food) {
        this.order = order;
        this.size = size;
        this.staticFood = staticFood;
        this.snakes = snakes;
        this.food = food;
    }

    @Override
    public int width() {
        return size().width();
    }

    @Override
    public int height() {
        return size().height();
    }

    @Override
    public int staticFood() {
        return staticFood;
    }

    @Override
    public Collection<Snake> getSnakes() {
        return snakes;
    }

    @Override
    public Collection<Pose> getFood() {
        return food;
    }

    @Override
    public int order() {
        return order;
    }

    @Override
    public FieldSize size() {
        return size;
    }

}
