package ru.sidey.snake.network.impl;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import ru.sidey.snake.SnakesProto;
import ru.sidey.snake.event.EventHandler;
import ru.sidey.snake.event.EventManager;
import ru.sidey.snake.network.ConnectedNode;
import ru.sidey.snake.network.Connection;
import ru.sidey.snake.network.event.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class NetworkNode implements ConnectedNode {

    private final Logger logger = LogManager.getLogger(NetworkNode.class);

    @NotNull
    private final AddressIdentifier address;

    private final AtomicInteger sequence = new AtomicInteger();

    @NotNull
    private final Connection socket;

    @NotNull
    private final Object pingMonitor = new Object();

    @NotNull
    private final Long pingTime;

    @NotNull
    private final Object deathMonitor = new Object();

    @NotNull
    private final Long deathTime;

    private final ScheduledExecutorService executorService;

    private ScheduledFuture<?> pingFuture;

    private ScheduledFuture<?> nodeKiller;

    private final Map<Long, ScheduledFuture<?>> senderTasks = new HashMap<>();

    private boolean isAlive = true;

    public NetworkNode(@NotNull AddressIdentifier address, @NotNull Connection socket, long pingTime, long deathTime) {
        this.address = address;
        this.socket = socket;
        this.pingTime = pingTime;
        this.deathTime = deathTime;
        executorService = Executors.newSingleThreadScheduledExecutor();
        pingFuture = executorService.schedule(this::sendPing, pingTime, TimeUnit.MILLISECONDS);
        nodeKiller = executorService.schedule(this::killNode, deathTime, TimeUnit.MILLISECONDS);
        EventManager.registerListener(this);
    }

    @Override
    public AddressIdentifier address() {
        return address;
    }

    public synchronized void stop() {
        isAlive = false;
        executorService.shutdownNow();
        EventManager.unregisterListener(this);
    }

    public synchronized boolean isAlive() {
        return isAlive;
    }

    @Override
    public void sendPacket(SnakesProto.GameMessage message) {
        synchronized (senderTasks) {
            final var msg = message.toBuilder().setMsgSeq(sequence.getAndIncrement()).build();
            try {
                ScheduledFuture<?> f = executorService.scheduleWithFixedDelay(() -> sendInternal(message), 0, pingTime, TimeUnit.MILLISECONDS);
                senderTasks.put(msg.getMsgSeq(), f);
            } catch (RejectedExecutionException ignore) {}
        }
    }

    private void sendInternal(SnakesProto.GameMessage message) {
        try {
            socket.sendMessage(address, message.toBuilder().setMsgSeq(sequence.getAndIncrement()).build());
            updatePing();
        } catch (IOException e) {
            logger.log(Level.WARN, "Packet send error", e);
        }
    }

    private void sendPing() {
        sendInternal(SnakesProto.GameMessage.newBuilder()
                .setPing(SnakesProto.GameMessage.PingMsg.getDefaultInstance())
                .setMsgSeq(sequence.getAndIncrement())
                .build()
        );
    }

    private void killNode() {
        stop();
        EventManager.runEvent(new NodeDeathEvent(this));
    }

    private void updatePing() {
        synchronized (pingMonitor) {
            pingFuture.cancel(false);
            pingFuture = executorService.schedule(this::sendPing, pingTime, TimeUnit.MILLISECONDS);
        }
    }

    private void updateDeath() {
        synchronized (deathMonitor) {
            nodeKiller.cancel(false);
            nodeKiller = executorService.schedule(this::killNode, deathTime, TimeUnit.MILLISECONDS);
        }
    }

    @EventHandler
    public void onAckMessage(AckMessageEvent e) {
        if (e.getAddress().equals(address())) {
            updateDeath();
            synchronized (senderTasks) {
                ScheduledFuture<?> f = senderTasks.remove(e.getMsgSeq());
                if (f != null) {
                    f.cancel(false);
                }
            }
        }
    }

    @EventHandler
    public void onAckMessage(DiscoverMessageEvent e) {
        if (e.getAddress().equals(address())) {
            updateDeath();
        }
    }

    @EventHandler
    public void onAckMessage(AnnouncementMessageEvent e) {
        if (e.getAddress().equals(address())) {
            updateDeath();
        }
    }

    @EventHandler
    public void onAckMessage(ErrorMessageEvent e) {
        if (e.getAddress().equals(address())) {
            updateDeath();
        }
    }

    @EventHandler
    public void onAckMessage(JoinMessageEvent e) {
        if (e.getAddress().equals(address())) {
            updateDeath();
        }
    }

    @EventHandler
    public void onAckMessage(PingMessageEvent e) {
        if (e.getAddress().equals(address())) {
            updateDeath();
        }
    }

    @EventHandler
    public void onAckMessage(RoleChangeMessageEvent e) {
        if (e.getAddress().equals(address())) {
            updateDeath();
        }
    }

    @EventHandler
    public void onAckMessage(StateMessageEvent e) {
        if (e.getAddress().equals(address())) {
            updateDeath();
        }
    }

    @EventHandler
    public void onAckMessage(SteerMessageEvent e) {
        if (e.getAddress().equals(address())) {
            updateDeath();
        }
    }

}
