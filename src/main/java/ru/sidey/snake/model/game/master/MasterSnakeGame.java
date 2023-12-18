package ru.sidey.snake.model.game.master;

import ru.sidey.snake.event.EventHandler;
import ru.sidey.snake.model.game.SnakeField;
import ru.sidey.snake.model.game.SnakeGame;
import ru.sidey.snake.model.game.object.SimpleSnake;
import ru.sidey.snake.model.game.object.Snake;
import ru.sidey.snake.model.game.player.Player;
import ru.sidey.snake.model.game.player.PlayerRole;
import ru.sidey.snake.model.game.player.PlayerType;
import ru.sidey.snake.network.ConnectedNode;
import ru.sidey.snake.network.event.NodeDeathEvent;
import ru.sidey.snake.network.impl.AddressIdentifier;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class MasterSnakeGame implements SnakeGame {

    private final String name;

    private final AtomicInteger idProducer = new AtomicInteger();

    private final int width;

    private final int height;

    private final int staticFood;

    private final int stateDelay;

    private MasterSnakeField field;

    private final Map<AddressIdentifier, MasterPlayer> addressPlayers = new HashMap<>();

    private final Map<Integer, MasterPlayer> idPlayers = new HashMap<>();

    private final Collection<MasterPlayer> players = new ArrayList<>();

    public MasterSnakeGame(String name, int width, int height, int staticFood, int stateDelay) {
        this.name = name;
        this.width = width;
        this.height = height;
        this.staticFood = staticFood;
        this.stateDelay = stateDelay;
    }

    public MasterSnakeGame(SnakeGame game) {
        this.name = game.name();
        this.width = game.getField().width();
        this.height = game.getField().height();
        this.staticFood = game.getField().staticFood();
        this.stateDelay = game.stateDelay();
    }

    public synchronized void stop() {

    }

    private synchronized void nextStep() {
        MasterSnakeField newField = field.nextStep();
        Collection<? extends Snake> snakes = newField.getSnakes();
        players.forEach(p -> {
            p.setSnake(null);
        });
        snakes.forEach(s -> {
            MasterPlayer pl = idPlayers.get(s.id());
            if (pl != null) {
                pl.setSnake(s);
            }
        });
        players.forEach(p -> {
            if (p.snake() == null && p.role() == PlayerRole.NORMAL) {
                p.changeRole(PlayerRole.VIEWER);
            }
        });
        this.field = newField;
    }

    public MasterPlayer addPlayer(ConnectedNode node, String name, PlayerRole role, boolean hasSnake) {
        MasterPlayer player = new MasterPlayer(
                idProducer.getAndIncrement(),
                name,
                PlayerType.HUMAN,
                node,
                role,
                null
        );
        addressPlayers.put(node.address(), player);
        players.add(player);
        idPlayers.put(player.id(), player);
        if (hasSnake) {
            field.createSnake(player).ifPresent(player::setSnake);
        }
        return player;
    }

    @EventHandler
    public synchronized void onNodeDeath(NodeDeathEvent e) {
        AddressIdentifier id = e.getNode().address();
        MasterPlayer pl = addressPlayers.remove(id);
        if (pl != null) {
            Snake newSnake = new SimpleSnake(pl.snake(), null);
            field.replaceSnake(newSnake);
            players.remove(pl);
            idPlayers.remove(pl.id());
        }
    }

    @Override
    public SnakeField getField() {
        return field;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public int stateDelay() {
        return stateDelay;
    }
}
