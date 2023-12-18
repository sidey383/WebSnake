package ru.sidey.snake.controller.event;

import ru.sidey.snake.SnakesProto;
import ru.sidey.snake.event.Event;

public class GameSelectEvent extends Event {

    private final SnakesProto.GameAnnouncement game;

    public GameSelectEvent(SnakesProto.GameAnnouncement game) {
        super(true);
        this.game = game;
    }

    public SnakesProto.GameAnnouncement getGame() {
        return game;
    }

}
