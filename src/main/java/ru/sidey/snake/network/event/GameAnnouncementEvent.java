package ru.sidey.snake.network.event;

import ru.sidey.snake.SnakesProto;
import ru.sidey.snake.event.Event;

public class GameAnnouncementEvent extends Event {

    private final SnakesProto.GameMessage.AnnouncementMsg msg;

    public GameAnnouncementEvent(SnakesProto.GameMessage.AnnouncementMsg msg) {
        super(true);
        this.msg = msg;
    }

    public SnakesProto.GameMessage.AnnouncementMsg getAnnouncement() {
        return msg;
    }

}
