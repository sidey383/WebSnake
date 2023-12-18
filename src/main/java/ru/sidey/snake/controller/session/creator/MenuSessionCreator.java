package ru.sidey.snake.controller.session.creator;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.sidey.snake.controller.ControllerSessionCreator;
import ru.sidey.snake.controller.session.MenuSession;
import ru.sidey.snake.event.EventHandler;
import ru.sidey.snake.view.event.MenuOpenEvent;
import ru.sidey.snake.view.scene.MenuView;

import java.io.IOException;

public class MenuSessionCreator extends ControllerSessionCreator {

    private final Logger logger = LogManager.getLogger(MenuSessionCreator.class);

    @EventHandler
    public void onMenuOpen(MenuOpenEvent e) {
        MenuView view;
        try {
           view = controller().getScene(MenuView.class);
        } catch (IOException ex) {
            logger.log(Level.ERROR, "Menu view create error", ex);
            return;
        }
        MenuSession session = new MenuSession(view);
        controller().setSession(session);
    }


}
