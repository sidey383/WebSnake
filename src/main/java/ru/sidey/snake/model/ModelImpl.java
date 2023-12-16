package ru.sidey.snake.model;

public class ModelImpl implements Model {

    GameListener listener = new GameListener();

    @Override
    public void startListenGames() {
        listener.start();
    }

    @Override
    public void stopListenGames() {
        listener.stop();
    }
}
