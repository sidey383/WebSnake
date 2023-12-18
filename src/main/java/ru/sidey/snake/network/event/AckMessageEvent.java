package ru.sidey.snake.network.event;

import ru.sidey.snake.SnakesProto;
import ru.sidey.snake.network.impl.AddressIdentifier;

import java.net.InetAddress;

public class AckMessageEvent extends GameMessageEvent<SnakesProto.GameMessage.AckMsg> {
    public AckMessageEvent(SnakesProto.GameMessage.AckMsg message, AddressIdentifier address, int senderId, long msgSeq, int receiverId) {
        super(message, address, senderId, msgSeq, receiverId);
    }
}
