package ru.sidey.snake.network.impl;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Parser;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.sidey.snake.SnakesProto;
import ru.sidey.snake.network.Connection;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class MainSocket implements Runnable, Connection {

    private final Logger logger = LogManager.getLogger(MainSocket.class);

    private final Parser<SnakesProto.GameMessage> parser = SnakesProto.GameMessage.parser();

    private final AddressIdentifier multicast;

    private final DatagramSocket socket;

    private final MessageHandler handler = new MessageHandler();

    private Thread receiverThread;

    public MainSocket(DatagramSocket socket) {
        this.socket = socket;
        try {
            multicast = new AddressIdentifier(InetAddress.getByName("239.192.0.4"), 9192);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized void start() {
        if (receiverThread != null) {
            return;
        }
        receiverThread = new Thread(this, "Socket");
    }

    public synchronized void stop() {
        if (receiverThread != null) {
            receiverThread.interrupt();
            try {
                receiverThread.join();
            } catch (InterruptedException e) {
                logger.log(Level.ERROR, "Socket tread join error", e);
            }
        }
    }


    @Override
    public void run() {
        byte[] buffer = new byte[64000];
        while (!Thread.currentThread().isInterrupted()) {
            try {
                DatagramPacket packet = new DatagramPacket(buffer, 0, buffer.length);
                socket.receive(packet);
                try {
                    var msg = parser.parseFrom(packet.getData(), 0, packet.getLength());
                    AddressIdentifier id = new AddressIdentifier(packet.getAddress(), packet.getPort());
                    handler.handle(msg, id);
                    if (msg.getTypeCase() != SnakesProto.GameMessage.TypeCase.ACK &&
                        msg.getTypeCase() != SnakesProto.GameMessage.TypeCase.DISCOVER) {
                        sendMessage(id, SnakesProto.GameMessage.newBuilder()
                                .setSenderId(msg.getReceiverId())
                                .setReceiverId(msg.getSenderId())
                                .setMsgSeq(msg.getMsgSeq())
                                .setAck(SnakesProto.GameMessage.AckMsg.newBuilder().build())
                                .build()
                        );
                    }
                } catch (InvalidProtocolBufferException e) {
                    logger.log(Level.INFO, "Invalid announcement message", e);

                }
            } catch (IOException e) {
                logger.log(Level.ERROR, "Network listener error", e);
            }
        }
    }

    public void sendMessage(AddressIdentifier address, SnakesProto.GameMessage message) throws IOException {
        byte[] data = message.toByteArray();
        DatagramPacket packet = new DatagramPacket(data, 0, data.length, address.address(), address.port());
        socket.send(packet);
    }

    public void sendAnnouncement(SnakesProto.GameMessage.AnnouncementMsg message) throws IOException {
        byte[] data = message.toByteArray();
        DatagramPacket packet = new DatagramPacket(data, 0, data.length, multicast.address(), multicast.port());
        socket.send(packet);
    }

}
