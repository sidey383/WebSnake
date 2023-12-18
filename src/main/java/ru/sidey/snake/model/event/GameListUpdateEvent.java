package ru.sidey.snake.model.event;

import ru.sidey.snake.SnakesProto;
import ru.sidey.snake.event.Event;

import java.util.Collection;
import java.util.Collections;

public class GameListUpdateEvent extends Event {

    private final Collection<SnakesProto.GameAnnouncement> announcements;

    public GameListUpdateEvent(Collection<SnakesProto.GameAnnouncement> announcements) {
        super(false);
        this.announcements = Collections.unmodifiableCollection(announcements);
    }

    public Collection<SnakesProto.GameAnnouncement> getAnnouncements() {
        return announcements;
    }
}
