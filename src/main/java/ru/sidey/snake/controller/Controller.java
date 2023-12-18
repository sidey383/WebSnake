package ru.sidey.snake.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.sidey.snake.controller.exception.ControllerException;
import ru.sidey.snake.event.EventHandler;
import ru.sidey.snake.event.EventManager;
import ru.sidey.snake.model.ModelInterface;
import ru.sidey.snake.view.AppScene;
import ru.sidey.snake.view.ViewInterface;
import ru.sidey.snake.view.event.WindowCloseEvent;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;

/**
 * Main controller class
 * Manage access to view
 * **/
public class Controller {

    private static final Logger logger = LogManager.getLogger(Controller.class);

    private final ViewInterface view;

    private final ModelInterface model;

    public final Collection<ControllerSessionCreator> sessionCreators = new HashSet<>();

    private ControllerSession session = null;

    public Controller(ViewInterface view, ModelInterface model) {
        this.view = view;
        this.model = model;
        EventManager.registerListener(this);
    }

    private <T extends ControllerModule> T startModule(T module) throws ControllerException {
        module.start(this);
        return module;
    }

    private void stopModule(ControllerModule module) {
        if (module == null)
            return;
        try {
            module.stop();
        } catch (ControllerException e) {
            logger.error("Controller module stop error", e);
        }
    }

    public synchronized void setSession(ControllerSession newSession) {
        try {
            ControllerSession oldSession = session;
            session = startModule(newSession);
            stopModule(oldSession);
        } catch (ControllerException e) {
            logger.error("Controller session start error ");
            return;
        }
        view.setScene(session.scene());
    }

    public synchronized void addSessionCreator(ControllerSessionCreator creator) {
        if (!sessionCreators.contains(creator)) {
            try {
                sessionCreators.add(startModule(creator));
            } catch (ControllerException e) {
                logger.error("Controller session creator start error", e);
            }
        }
    }

    public synchronized void removeSessionCreator(ControllerSessionCreator creator) {
        if (sessionCreators.remove(creator))
            stopModule(creator);
    }

    @EventHandler
    public void onWindowsClose(WindowCloseEvent e) {
        end();
    }

    public synchronized ControllerSession session() {
        return session;
    }

    public <T extends AppScene> T getScene(Class<T> clazz) throws IOException {
        return view.getScene(clazz);
    }

    public ModelInterface model() {
        return model;
    }

    public synchronized void end() {
        stopModule(session);
        view.close();
        for (ControllerSessionCreator creator : sessionCreators) {
            stopModule(creator);
        }
        EventManager.unregisterListener(this);
    }

}
