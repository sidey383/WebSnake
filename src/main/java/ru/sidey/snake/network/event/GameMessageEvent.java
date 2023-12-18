package ru.sidey.snake.network.event;

import ru.sidey.snake.event.Event;
import ru.sidey.snake.network.impl.AddressIdentifier;

import java.net.InetAddress;

public abstract class GameMessageEvent<T> extends Event {

    private final AddressIdentifier address;

    private final T message;

    private final int senderId;

    private final long msgSeq;

    private final int receiverId;

    public GameMessageEvent(T message, AddressIdentifier address, int senderId, long msgSeq, int receiverId) {
        super(true);
        this.address = address;
        this.message = message;
        this.senderId = senderId;
        this.msgSeq = msgSeq;
        this.receiverId = receiverId;
    }


    public AddressIdentifier getAddress() {
        return address;
    }

    public T getMessage() {
        return message;
    }

    public int getSenderId() {
        return senderId;
    }

    public long getMsgSeq() {
        return msgSeq;
    }

    public int getReceiverId() {
        return receiverId;
    }
}
