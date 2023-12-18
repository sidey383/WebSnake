package ru.sidey.snake.controller.exception;

public class GameAlreadyStartedException extends ControllerException {

    public GameAlreadyStartedException(String message) {
        super(message);
    }

}
