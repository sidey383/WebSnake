package ru.sidey.snake.network.event;

import ru.sidey.snake.SnakesProto;
import ru.sidey.snake.network.impl.AddressIdentifier;

public class PingMessageEvent extends GameMessageEvent<SnakesProto.GameMessage.PingMsg> {

    public PingMessageEvent(SnakesProto.GameMessage.PingMsg message, AddressIdentifier address, int senderId, long msgSeq, int receiverId) {
        super(message, address, senderId, msgSeq, receiverId);
    }
}
