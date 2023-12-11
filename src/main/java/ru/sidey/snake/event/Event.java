package ru.sidey.snake.event;

public abstract class Event {

    private final boolean isAsync;

    public Event(boolean isAsync) {
        this.isAsync = isAsync;
    }

    public boolean isAsynchronous() {
        return isAsync;
    }

}
