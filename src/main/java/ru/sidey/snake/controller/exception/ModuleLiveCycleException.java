package ru.sidey.snake.controller.exception;

import ru.sidey.snake.controller.ControllerModule;

public class ModuleLiveCycleException extends ControllerException {

    public ModuleLiveCycleException(String message, ControllerModule module) {
        super(module + " : " + message);
    }

}
