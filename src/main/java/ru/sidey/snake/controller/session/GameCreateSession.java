package ru.sidey.snake.controller.session;

import org.jetbrains.annotations.NotNull;
import ru.sidey.snake.controller.ControllerSession;
import ru.sidey.snake.event.EventHandler;
import ru.sidey.snake.view.AppScene;
import ru.sidey.snake.view.event.GameCreateEvent;
import ru.sidey.snake.view.scene.GameCreateView;

public class GameCreateSession extends ControllerSession {

    private final GameCreateView view;

    public GameCreateSession(@NotNull GameCreateView view) {
        this.view = view;
    }

    @EventHandler
    public void onGameCreate(GameCreateEvent e) {
        System.out.println("Create game");
    }

    @Override
    public AppScene scene() {
        return view;
    }
}
