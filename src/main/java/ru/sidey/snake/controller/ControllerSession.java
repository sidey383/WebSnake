package ru.sidey.snake.controller;

import ru.sidey.snake.view.AppScene;


/**
 * Controller session module. Manages a separate scene.
 * **/
public abstract class ControllerSession extends ControllerModule {

    public abstract AppScene scene();

}
