package ru.sidey.snake.network;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.sidey.snake.SnakesProto;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Supplier;

public class AnnouncementNetworkSender extends TimerTask implements AnnouncementSender {

    private final Logger logger = LogManager.getLogger(AnnouncementNetworkSender.class);

    private final InetAddress address;

    private final int port;

    private Timer timer;

    private Supplier<SnakesProto.GameMessage.AnnouncementMsg> msgSupplier;

    public AnnouncementNetworkSender(InetAddress address, int port) {
        this.address = address;
        this.port = port;
        if (port < 0 || port > 65535)
            throw new IllegalArgumentException("Wrong port " + port);
        if (!address.isMulticastAddress())
            throw new IllegalArgumentException("Not multicast address");
    }

    public synchronized void start(Supplier<SnakesProto.GameMessage.AnnouncementMsg> msgSupplier) {
        synchronized (this) {
            this.msgSupplier = msgSupplier;
            if (timer != null)
                timer.cancel();
            timer = new Timer();
            timer.scheduleAtFixedRate(this, 0, 1000);
        }
    }

    @Override
    public synchronized void stop() {
       synchronized (this) {
           if (timer != null)
               timer.cancel();
           this.msgSupplier = null;
       }
    }

    public void run() {
        synchronized (this) {
            if (msgSupplier == null)
                return;
            try {
                DatagramSocket socket = new DatagramSocket();
                byte[] data = msgSupplier.get().toByteArray();
                DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
                socket.send(packet);
                socket.close();
            } catch (IOException e) {
                logger.log(Level.ERROR, "Announcement send error", e);
            }
        }
    }

}
