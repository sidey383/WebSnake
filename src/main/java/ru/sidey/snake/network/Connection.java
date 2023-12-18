package ru.sidey.snake.network;

import ru.sidey.snake.SnakesProto;
import ru.sidey.snake.network.impl.AddressIdentifier;

import java.io.IOException;

public interface Connection {

    void start();
    void stop();

    void sendMessage(AddressIdentifier address, SnakesProto.GameMessage message) throws IOException;

    public void sendAnnouncement(SnakesProto.GameMessage.AnnouncementMsg message) throws IOException;

}
