package ru.sidey.snake.controller;

import ru.sidey.snake.controller.exception.ControllerException;
import ru.sidey.snake.controller.exception.ModuleLiveCycleException;
import ru.sidey.snake.event.EventManager;

/**
 * The controller module. The life cycle of the controller is directly related to a specific module.
 * **/
public abstract class ControllerModule {

    private boolean isStarted = false;

    private boolean isWorking = false;

    private Controller controller;

    /**
     * Part of the module lifecycle. Can only be called once.
     * This method should be called before all others.
     * **/
    protected void start(Controller controller) throws ControllerException {
        if (isStarted)
            throw new ModuleLiveCycleException("This controller module already started", this);
        isStarted = true;
        isWorking = true;
        EventManager.registerListener(this);
        this.controller = controller;
    }

    /**
     * Part of the module lifecycle. Can only be called once.
     * After calling this method, no other methods should be called.
     * **/
    protected void stop() throws ControllerException {
        if (!isWorking)
            throw new ModuleLiveCycleException("This controller module already ended", this);
        if (!isStarted)
            throw new ModuleLiveCycleException("This controller module has not started yet", this);
        isWorking = false;
        EventManager.unregisterListener(this);
    }

    /**
     * returns null before calling start
     * @return associated {@link Controller}
     * **/
    public Controller controller() {
        if (!isStarted)
            throw new IllegalStateException("The module is not started, it is impossible to get the controller");
        return controller;
    }

}
