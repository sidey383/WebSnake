package ru.sidey.snake.controller.session.creator;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.sidey.snake.controller.ControllerSessionCreator;
import ru.sidey.snake.controller.session.GameCreateSession;
import ru.sidey.snake.event.EventHandler;
import ru.sidey.snake.view.event.GameCreateMenuOpen;
import ru.sidey.snake.view.scene.GameCreateView;

import java.io.IOException;

public class GameCreateSessionCreator extends ControllerSessionCreator {

    private final Logger logger = LogManager.getLogger(GameCreateSessionCreator.class);

    @EventHandler
    public void onOpenEvent(GameCreateMenuOpen e) {
        GameCreateView view;
        try {
            view = controller().getScene(GameCreateView.class);
        } catch (IOException ex) {
            logger.log(Level.ERROR, "Menu view create error", ex);
            return;
        }
        GameCreateSession session = new GameCreateSession(view);
        controller().setSession(session);
    }

}
