package ru.sidey.snake.network.event;

import ru.sidey.snake.SnakesProto;
import ru.sidey.snake.network.impl.AddressIdentifier;
public class DiscoverMessageEvent extends GameMessageEvent<SnakesProto.GameMessage.DiscoverMsg> {
    public DiscoverMessageEvent(SnakesProto.GameMessage.DiscoverMsg message, AddressIdentifier address, int senderId, long msgSeq, int receiverId) {
        super(message, address, senderId, msgSeq, receiverId);
    }
}
