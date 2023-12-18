package ru.sidey.snake.network.event;

import ru.sidey.snake.SnakesProto;
import ru.sidey.snake.network.impl.AddressIdentifier;

public class RoleChangeMessageEvent extends GameMessageEvent<SnakesProto.GameMessage.RoleChangeMsg> {
    public RoleChangeMessageEvent(SnakesProto.GameMessage.RoleChangeMsg message, AddressIdentifier address, int senderId, long msgSeq, int receiverId) {
        super(message, address, senderId, msgSeq, receiverId);
    }
}
