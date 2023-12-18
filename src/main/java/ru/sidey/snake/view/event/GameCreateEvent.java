package ru.sidey.snake.view.event;

import ru.sidey.snake.event.Event;

public class GameCreateEvent extends Event {

    private final String name;
    private final int width;
    private final int height;
    private final int food;

    public GameCreateEvent(String name, int width, int height, int food) {
        super(true);
        this.name = name;
        this.width = width;
        this.height = height;
        this.food = food;
    }

    public String getName() {
        return name;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getFood() {
        return food;
    }
}
