package ru.sidey.snake.model;

import ru.sidey.snake.network.Connection;
import ru.sidey.snake.network.impl.MainSocket;

import java.net.DatagramSocket;
import java.net.SocketException;

public class Model implements ModelInterface {

    private final GameListener listener = new GameListener();

    private final Connection connection;

    public Model() throws SocketException {
        connection = new MainSocket(new DatagramSocket());
    }

    @Override
    public void startListenGames() {
        listener.start();
    }

    @Override
    public void stopListenGames() {
        listener.stop();
    }

    public Connection getConnection() {
        return connection;
    }

}
