package ru.sidey.snake.network.event;

import ru.sidey.snake.SnakesProto;
import ru.sidey.snake.network.impl.AddressIdentifier;
public class JoinMessageEvent extends GameMessageEvent<SnakesProto.GameMessage.JoinMsg> {
    public JoinMessageEvent(SnakesProto.GameMessage.JoinMsg message, AddressIdentifier address, int senderId, long msgSeq, int receiverId) {
        super(message, address, senderId, msgSeq, receiverId);
    }
}
