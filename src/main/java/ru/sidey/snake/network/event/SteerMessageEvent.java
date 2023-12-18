package ru.sidey.snake.network.event;

import ru.sidey.snake.SnakesProto;
import ru.sidey.snake.network.impl.AddressIdentifier;
public class SteerMessageEvent extends GameMessageEvent<SnakesProto.GameMessage.SteerMsg> {
    public SteerMessageEvent(SnakesProto.GameMessage.SteerMsg message, AddressIdentifier address, int senderId, long msgSeq, int receiverId) {
        super(message, address, senderId, msgSeq, receiverId);
    }
}
