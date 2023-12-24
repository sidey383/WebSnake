package ru.sidey.snake.model.game.master;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.sidey.snake.SnakesProto;
import ru.sidey.snake.event.EventHandler;
import ru.sidey.snake.event.EventManager;
import ru.sidey.snake.model.game.*;
import ru.sidey.snake.model.game.master.event.DeputyChangeEvent;
import ru.sidey.snake.network.Connection;
import ru.sidey.snake.network.event.DiscoverMessageEvent;
import ru.sidey.snake.network.event.JoinMessageEvent;
import ru.sidey.snake.network.event.SteerMessageEvent;
import ru.sidey.snake.network.impl.NetworkNode;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class MasterGame {

    private static final int BROADCAST_TIME = 1000;

    private final Logger logger = LogManager.getLogger(MasterGame.class);

    private final Connection connection;

    private final String gameName;

    private final int updateDelay;

    private final ScheduledExecutorService executorService;

    private final ScheduledFuture<?> updateTask;

    private final ScheduledFuture<?> broadcastTask;

    private final MasterField field;

    private final PlayerController playerController;

    public MasterGame(String masterName, Connection connection, String gameName, int updateDelay, FieldConfig config) {
        this.connection = connection;
        this.gameName = gameName;
        playerController = new PlayerController();
        this.updateDelay = updateDelay;
        playerController.initMaster(masterName);
        field = new MasterField(config);
        field.createSnake(playerController.master());
        executorService = Executors.newSingleThreadScheduledExecutor();
        updateTask = executorService.scheduleAtFixedRate(this::update, updateDelay, updateDelay, TimeUnit.MILLISECONDS);
        broadcastTask = executorService.scheduleAtFixedRate(this::broadcast, 0, BROADCAST_TIME, TimeUnit.MILLISECONDS);
        EventManager.registerListener(this);
    }


    private void addSnakePlayer(List<SnakesProto.GamePlayer> players, NodePlayer pl, Snake sn) {
        players.add(
                SnakesProto.GamePlayer.newBuilder()
                        .setId(pl.id())
                        .setRole(
                                pl == playerController.deputy() ? SnakesProto.NodeRole.DEPUTY :
                                        sn == null ? SnakesProto.NodeRole.VIEWER : SnakesProto.NodeRole.NORMAL)
                        .setScore(sn == null ? 0 : sn.score())
                        .setName(pl.name())
                        .setPort(pl.node().address().port())
                        .setIpAddress(pl.node().address().address().getHostAddress())
                        .setType(SnakesProto.PlayerType.HUMAN)
                        .build()
        );
    }

    public synchronized SnakesProto.GameState getState() {
        List<SnakesProto.GamePlayer> players = new ArrayList<>();
        List<SnakesProto.GameState.Snake> snakes = new ArrayList<>();
        Map<Integer, Snake> snakeMap = field.snakes().stream().collect(Collectors.toMap(Snake::ownerId, s -> s, (s1, s2) -> s1));
        for (NodePlayer pl : playerController.players()) {
            Snake sn = snakeMap.remove(pl.id());
            if (sn != null) {
                snakes.add(SnakesProto.GameState.Snake.newBuilder()
                        .addAllPoints(coordsFromBody(sn.body()))
                        .setHeadDirection(sn.direction().toProto())
                        .setState(SnakesProto.GameState.Snake.SnakeState.ALIVE)
                        .build());
            }
            addSnakePlayer(players, pl, sn);
        }
        MasterPlayer masterPlayer = playerController.master();
        Snake masterSnake = snakeMap.remove(masterPlayer.id());
        if (masterSnake != null) {
            snakes.add(SnakesProto.GameState.Snake.newBuilder()
                    .addAllPoints(coordsFromBody(masterSnake.body()))
                    .setHeadDirection(masterSnake.direction().toProto())
                    .setState(SnakesProto.GameState.Snake.SnakeState.ALIVE)
                    .build()
            );
        }
        players.add(
                SnakesProto.GamePlayer.newBuilder()
                        .setId(masterPlayer.id())
                        .setRole(SnakesProto.NodeRole.MASTER)
                        .setScore(masterSnake == null ? 0 : masterSnake.score())
                        .setName(masterPlayer.name())
                        .setType(SnakesProto.PlayerType.HUMAN)
                        .build()
        );
        for (Snake sn : snakeMap.values()) {
            snakes.add(SnakesProto.GameState.Snake.newBuilder()
                    .addAllPoints(coordsFromBody(sn.body()))
                    .setHeadDirection(sn.direction().toProto())
                    .setState(SnakesProto.GameState.Snake.SnakeState.ZOMBIE)
                    .build());
        }
        return SnakesProto.GameState.newBuilder()
                .addAllFoods(field.food().stream().map(f -> toCoord(f.x(), f.y())).toList())
                .addAllSnakes(snakes)
                .setPlayers(
                        SnakesProto.GamePlayers.newBuilder()
                                .addAllPlayers(players)
                                .build()
                )
                .setStateOrder(field.stateOrder())
                .build();
    }

    public synchronized SnakesProto.GameAnnouncement getAnnouncement() {
        List<SnakesProto.GamePlayer> players = new ArrayList<>();
        Map<Integer, Snake> snakeMap = field.snakes().stream().collect(Collectors.toMap(Snake::ownerId, s -> s, (s1, s2) -> s1));
        for (NodePlayer pl : playerController.players()) {
            Snake sn = snakeMap.remove(pl.id());
            addSnakePlayer(players, pl, sn);
        }
        MasterPlayer masterPlayer = playerController.master();
        Snake masterSnake = snakeMap.remove(masterPlayer.id());
        players.add(
                SnakesProto.GamePlayer.newBuilder()
                        .setId(masterPlayer.id())
                        .setRole(SnakesProto.NodeRole.MASTER)
                        .setScore(masterSnake == null ? 0 : masterSnake.score())
                        .setName(masterPlayer.name())
                        .setType(SnakesProto.PlayerType.HUMAN)
                        .build()
        );
        return SnakesProto.GameAnnouncement.newBuilder()
                .setCanJoin(field.hasPlace())
                .setGameName(gameName)
                .setConfig(
                        SnakesProto.GameConfig.newBuilder()
                                .setHeight(field.config().height())
                                .setWidth(field.config().width())
                                .setFoodStatic(field.config().foodStatic())
                                .setStateDelayMs(updateDelay)
                                .build()
                )
                .setPlayers(
                        SnakesProto.GamePlayers.newBuilder()
                                .addAllPlayers(players)
                                .build()
                )
                .build();
    }

    private List<SnakesProto.GameState.Coord> coordsFromBody(Collection<Pose> poses) {
        List<SnakesProto.GameState.Coord> result = new ArrayList<>();
        Pose control = null;
        Pose last = null;
        for (Pose p : poses) {
            if (control == null) {
                SnakesProto.GameState.Coord c = toCoord(p.x(), p.y());
                result.add(c);
                control = p;
                continue;
            }
            if (last == null) {
                last = p;
                continue;
            }
            if ((control.getXDiff(last) == 0 && control.getXDiff(p) == 0) ||
                (control.getYDiff(last) == 0 && control.getYDiff(p) == 0)
            ) {
                last = p;
            } else {
                SnakesProto.GameState.Coord c = toCoord(control.getXDiff(last), control.getYDiff(last));
                result.add(c);
                control = last;
                last = p;
            }
        }
        if (control != null && last != null) {
            SnakesProto.GameState.Coord c = toCoord(control.getXDiff(last), control.getYDiff(last));
            result.add(c);
        }
        return result;
    }

    private SnakesProto.GameState.Coord toCoord(int x, int y) {
        return SnakesProto.GameState.Coord.newBuilder()
                .setX(x)
                .setY(y)
                .build();
    }

    private synchronized void update() {
        field.nextStep();
        SnakesProto.GameState state = getState();
        for (Player player : playerController.players()) {
            player.sendState(state);
        }
        try {
            connection.sendAnnouncement(SnakesProto.GameMessage.AnnouncementMsg.newBuilder().addGames(getAnnouncement()).build());
        } catch (IOException e) {
            logger.log(Level.ERROR, "Announcement send error", e);
        }
    }

    private synchronized void broadcast() {
        try {
            connection.sendAnnouncement(SnakesProto.GameMessage.AnnouncementMsg.newBuilder().addGames(getAnnouncement()).build());
        } catch (IOException e) {
            logger.log(Level.ERROR, "Broadcast announcement error", e);
        }
    }

    @EventHandler
    public void onDeputyChange(DeputyChangeEvent e) {
        if (e.getDeputy() != null) {
            e.getDeputy().sendRoleChange
                    (SnakesProto.GameMessage.RoleChangeMsg.newBuilder()
                            .setReceiverRole(SnakesProto.NodeRole.DEPUTY)
                            .setSenderRole(SnakesProto.NodeRole.MASTER)
                            .build()
                    );
        }
    }

    @EventHandler
    public synchronized void onPlayerJoin(JoinMessageEvent e) {
        Optional<NodePlayer> optPlayer = playerController.getPlayer(e.getAddress());
        final NodePlayer player;
        final NetworkNode node;
        if (optPlayer.isEmpty()) {
            node = new NetworkNode(e.getAddress(), connection, updateDelay / 10, updateDelay * 8L / 10);
            player = playerController.addPlayer(e.getMessage().getPlayerName(), node);
        } else {
            player = optPlayer.get();
            node = player.node();
        }
        if (e.getMessage().getRequestedRole() != SnakesProto.NodeRole.VIEWER) {
            Optional<Snake> sn = field.createSnake(player);
            if (sn.isEmpty()) {
                node.sendPacket(
                        SnakesProto.GameMessage.newBuilder()
                                .setError(
                                        SnakesProto.GameMessage.ErrorMsg.newBuilder()
                                                .setErrorMessage("Can't create snake")
                                                .build()
                                )
                                .setSenderId(playerController.master().id())
                                .setReceiverId(player.id())
                                .build());
                return;
            }
        }
        node.sendPacket(
                SnakesProto.GameMessage.newBuilder()
                        .setAck(
                                SnakesProto.GameMessage.AckMsg.newBuilder()
                                        .build()
                        )
                        .setSenderId(playerController.master().id())
                        .setReceiverId(player.id())
                        .build());
    }

    @EventHandler
    public synchronized void onDiscoverMessage(DiscoverMessageEvent e) {
        try {
            connection.sendMessage(e.getAddress(), SnakesProto.GameMessage.newBuilder()
                    .setAnnouncement(
                            SnakesProto.GameMessage.AnnouncementMsg.newBuilder()
                                    .addGames(getAnnouncement())
                                    .build()
                    )
                    .build());
        } catch (IOException ex) {
            logger.log(Level.WARN, "Can't send answer on discover message", ex);
        }
    }

    @EventHandler
    public synchronized void onPlayerSteer(SteerMessageEvent e) {
        Optional<NodePlayer> optPlayer = playerController.getPlayer(e.getAddress());
        if (optPlayer.isEmpty())
            return;
        NodePlayer player = optPlayer.get();
        if (!player.applySteer(e.getMsgSeq()))
            return;
        Direction dir = Direction.fromProto(e.getMessage().getDirection());
        field.snakes().stream().filter(s -> s.ownerId() == player.id()).findFirst().ifPresent(s -> {
            if (s.direction().opposite() == dir)
                return;
            s.changeDirection(dir);
        });
    }

    public void stop() {
        updateTask.cancel(false);
        broadcastTask.cancel(false);
        executorService.shutdownNow();
        EventManager.unregisterListener(this);
    }

}
