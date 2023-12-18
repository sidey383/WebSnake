package ru.sidey.snake.network.impl;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Parser;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.sidey.snake.SnakesProto;
import ru.sidey.snake.event.EventManager;
import ru.sidey.snake.network.AnnouncementReceiver;
import ru.sidey.snake.network.event.GameAnnouncementEvent;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketTimeoutException;

public class AnnouncementNetworkReceiver implements AnnouncementReceiver, Runnable {

    private static final ThreadGroup THREAD_GROUP = new ThreadGroup("AnnouncementNetworkReceiver");

    private final Logger logger = LogManager.getLogger(AnnouncementNetworkReceiver.class);

    private final InetAddress address;

    private final int port;

    private Thread senderThread;

    public AnnouncementNetworkReceiver(InetAddress address, int port) {
        this.address = address;
        this.port = port;
        if (!address.isMulticastAddress()) {
            throw new IllegalArgumentException("Not multicast address");
        }
    }

    @Override
    public void run() {
        logger.log(Level.INFO, "Announcement network receiver start listen");
        byte[] buffer = new byte[16000];
        Parser<SnakesProto.GameMessage.AnnouncementMsg> parser = SnakesProto.GameMessage.AnnouncementMsg.parser();
        try (MulticastSocket socket = new MulticastSocket(port)) {
            socket.joinGroup(address);
            socket.setSoTimeout(200);
            while (!Thread.currentThread().isInterrupted()) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                try {
                    socket.receive(packet);
                } catch (SocketTimeoutException e) {
                    continue;
                }
                try {
                    var msg = parser.parseFrom(packet.getData(), 0, packet.getLength());
                    EventManager.runEvent(new GameAnnouncementEvent(msg));
                } catch (InvalidProtocolBufferException e) {
                    logger.log(Level.INFO, "Invalid announcement message", e);
                }
            }
        } catch (IOException e) {
            logger.log(Level.ERROR, "Announcement network receiver error", e);
        }
        logger.log(Level.INFO, "Announcement network receiver stop listen");
    }

    @Override
    public synchronized void stop() {
        if (senderThread != null) {
            senderThread.interrupt();
            try {
                senderThread.join();
            } catch (InterruptedException e) {
                logger.log(Level.ERROR, e);
            }
        }
        senderThread = null;
    }

    public synchronized void start() {
        if (senderThread != null) {
            senderThread.interrupt();
            try {
                senderThread.join();
            } catch (InterruptedException e) {
                logger.log(Level.ERROR, e);
            }
        }
        senderThread = new Thread(THREAD_GROUP, this);
        senderThread.start();
    }

}
