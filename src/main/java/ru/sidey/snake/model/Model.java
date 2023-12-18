package ru.sidey.snake.model;

public class Model implements ModelInterface {

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
