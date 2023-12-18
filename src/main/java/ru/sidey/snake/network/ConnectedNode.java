package ru.sidey.snake.network;

import ru.sidey.snake.SnakesProto;
import ru.sidey.snake.network.impl.AddressIdentifier;

public interface ConnectedNode {

    AddressIdentifier address();

    void sendPacket(SnakesProto.GameMessage message);

    boolean isAlive();

}
