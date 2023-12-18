package ru.sidey.snake.model.game.object;

import ru.sidey.snake.model.game.FieldSize;

public record Pose(int x, int y, FieldSize size) {

    public Pose(int x, int y, FieldSize size) {
        this.x = (x + size.width()) % size.width();
        this.y = (y + size.height()) % size.height();
        this.size = size;
    }

    public Pose add(Direction direction) {
        return add(direction.x(), direction.y());
    }

    public Pose add(int x, int y) {
        return new Pose(
                Math.floorMod(this.x + x, size.width()),
                Math.floorMod(this.y + y, size.height()),
                size
        );
    }

}
