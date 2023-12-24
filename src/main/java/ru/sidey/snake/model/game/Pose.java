package ru.sidey.snake.model.game;

public record Pose(int x, int y, Size size) {

    public Pose(int x, int y, Size size) {
        this.size = size;
        this.x = normX(x);
        this.y = normY(y);
    }

    public Pose applyDirection(Direction direction) {
        return new Pose(x + direction.getX(), y + direction.getY(), size);
    }

    public Pose apply(int x, int y) {
        if (x == 0 && y == 0)
            return this;
        return new Pose(x + this.x, y + this.y, size);
    }

    public int getXDiff(Pose p) {
        return (p.x - x) % size.width();
    }

    public int getYDiff(Pose p) {
        return (p.y - y) % size.height();
    }

    public int minDistance(Pose pose) {
        return Math.min(normX(x - pose.x), normY(y - pose.y));
    }

    private int normX(int x) {
        return Math.abs(x % size.width());
    }

    private int normY(int y) {
        return Math.abs(y % size.height());
    }

}
