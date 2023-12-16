package ru.sidey.snake.model;

import ru.sidey.snake.SnakesProto;
import ru.sidey.snake.network.AnnouncementNetworkSender;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class GameHolder {

    private final List<SnakesProto.GamePlayer> players;

    private final AtomicInteger idProducer = new AtomicInteger();

    private final boolean canJoin;

    private final String gameName;

    private final int width;

    private final int height;

    private final int foodStatic;

    private final int stateDelayMs;

    private final SnakesProto.GameConfig config;

    private final AnnouncementNetworkSender announcementNetworkSender;

    public GameHolder(String gameName, String ownerName, int width, int height, int foodStatic, int stateDelayMs) {
        try {
            announcementNetworkSender = new AnnouncementNetworkSender(InetAddress.getByName("239.192.0.4"), 9192);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        this.width = width;
        this.height = height;
        this.foodStatic = foodStatic;
        this.stateDelayMs = stateDelayMs;
        this.players = new ArrayList<>();
        this.players.add(SnakesProto.GamePlayer.newBuilder()
                .setName(ownerName)
                .setRole(SnakesProto.NodeRole.MASTER)
                .setScore(0)
                .setId(idProducer.getAndIncrement())
                .build()
        );
        this.canJoin = true;
        this.gameName = gameName;
        config = SnakesProto.GameConfig.newBuilder()
                .setFoodStatic(foodStatic)
                .setWidth(width)
                .setHeight(height)
                .setStateDelayMs(stateDelayMs)
                .build();
    }

    public boolean isCanJoin() {
        return canJoin;
    }

    public String getGameName() {
        return gameName;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getFoodStatic() {
        return foodStatic;
    }

    public int getStateDelayMs() {
        return stateDelayMs;
    }

    public void start() {
        announcementNetworkSender.start(this::produceAnnouncement);
    }

    public void stop() {
        announcementNetworkSender.stop();
    }

    private synchronized SnakesProto.GameMessage.AnnouncementMsg produceAnnouncement() {
        return SnakesProto.GameMessage.AnnouncementMsg.newBuilder()
                .addGames(
                        SnakesProto.GameAnnouncement.newBuilder()
                                .setPlayers(
                                        SnakesProto.GamePlayers.newBuilder()
                                                .addAllPlayers(players)
                                )
                                .setConfig(config)
                                .setCanJoin(canJoin)
                                .setGameName(gameName)
                                .build()
                ).build();
    }

}
