package ru.sidey.snake.network.event;

import ru.sidey.snake.SnakesProto;
import ru.sidey.snake.network.impl.AddressIdentifier;

import java.net.InetAddress;

public class ErrorMessageEvent extends GameMessageEvent<SnakesProto.GameMessage.ErrorMsg> {
    public ErrorMessageEvent(SnakesProto.GameMessage.ErrorMsg message, AddressIdentifier address, int senderId, long msgSeq, int receiverId) {
        super(message, address, senderId, msgSeq, receiverId);
    }
}
