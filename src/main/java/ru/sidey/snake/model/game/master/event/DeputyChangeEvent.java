package ru.sidey.snake.model.game.master.event;

import org.jetbrains.annotations.Nullable;
import ru.sidey.snake.event.Event;
import ru.sidey.snake.model.game.Player;

public class DeputyChangeEvent extends Event {

    @Nullable
    private final Player player;

    public DeputyChangeEvent(@Nullable Player player) {
        super(false);
        this.player = player;
    }

    @Nullable
    public Player getDeputy() {
        return player;
    }
}
