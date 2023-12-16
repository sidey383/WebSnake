package ru.sidey.snake.model.event;

import ru.sidey.snake.SnakesProto;
import ru.sidey.snake.event.Event;

import java.util.Collection;
import java.util.Collections;

public class GameListUpdateEvent extends Event {

    private final Collection<SnakesProto.GameMessage.AnnouncementMsg> announcements;

    public GameListUpdateEvent(Collection<SnakesProto.GameMessage.AnnouncementMsg> announcements) {
        super(false);
        this.announcements = Collections.unmodifiableCollection(announcements);
    }

    public Collection<SnakesProto.GameMessage.AnnouncementMsg> getAnnouncements() {
        return announcements;
    }
}
