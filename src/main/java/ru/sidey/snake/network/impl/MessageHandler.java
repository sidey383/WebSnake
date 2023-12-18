package ru.sidey.snake.network.impl;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.sidey.snake.SnakesProto;
import ru.sidey.snake.event.EventManager;
import ru.sidey.snake.network.event.*;

public class MessageHandler {

    private final Logger logger = LogManager.getLogger(MessageHandler.class);

    void handle(SnakesProto.GameMessage msg, AddressIdentifier address) {
        switch (msg.getTypeCase()) {
            case STEER -> EventManager.runEvent(
                    new SteerMessageEvent(
                            msg.getSteer(),
                            address,
                            msg.getSenderId(),
                            msg.getMsgSeq(),
                            msg.getReceiverId()
                    )
            );
            case ACK -> EventManager.runEvent(
                    new AckMessageEvent(
                            msg.getAck(),
                            address,
                            msg.getSenderId(),
                            msg.getMsgSeq(),
                            msg.getReceiverId()
                    )
            );
            case STATE -> EventManager.runEvent(
                    new StateMessageEvent(
                            msg.getState(),
                            address,
                            msg.getSenderId(),
                            msg.getMsgSeq(),
                            msg.getReceiverId()
                    )
            );
            case ERROR -> EventManager.runEvent(
                    new ErrorMessageEvent(
                            msg.getError(),
                            address,
                            msg.getSenderId(),
                            msg.getMsgSeq(),
                            msg.getReceiverId()
                    )
            );
            case ANNOUNCEMENT -> EventManager.runEvent(
                    new AnnouncementMessageEvent(
                            msg.getAnnouncement(),
                            address,
                            msg.getSenderId(),
                            msg.getMsgSeq(),
                            msg.getReceiverId()
                    )
            );
            case PING -> EventManager.runEvent(
                    new PingMessageEvent(
                            msg.getPing(),
                            address,
                            msg.getSenderId(),
                            msg.getMsgSeq(),
                            msg.getReceiverId()
                    )
            );
            case DISCOVER -> EventManager.runEvent(
                    new DiscoverMessageEvent(
                            msg.getDiscover(),
                            address,
                            msg.getSenderId(),
                            msg.getMsgSeq(),
                            msg.getReceiverId()
                    )
            );
            case JOIN -> EventManager.runEvent(
                    new JoinMessageEvent(
                            msg.getJoin(),
                            address,
                            msg.getSenderId(),
                            msg.getMsgSeq(),
                            msg.getReceiverId()
                    )
            );
            case ROLE_CHANGE -> EventManager.runEvent(
                    new RoleChangeMessageEvent(
                            msg.getRoleChange(),
                            address,
                            msg.getSenderId(),
                            msg.getMsgSeq(),
                            msg.getReceiverId()
                    )
            );
            case TYPE_NOT_SET ->
                    logger.log(Level.WARN, "Client " + address + "Send game message without type");
        }
    }

}
