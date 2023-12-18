package ru.sidey.snake.network.event;

import ru.sidey.snake.SnakesProto;
import ru.sidey.snake.network.impl.AddressIdentifier;

import java.net.InetAddress;

public class AnnouncementMessageEvent extends GameMessageEvent<SnakesProto.GameMessage.AnnouncementMsg> {
    public AnnouncementMessageEvent(SnakesProto.GameMessage.AnnouncementMsg message, AddressIdentifier address, int senderId, long msgSeq, int receiverId) {
        super(message, address, senderId, msgSeq, receiverId);
    }
}
