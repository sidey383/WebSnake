package ru.sidey.snake.model.game.master;

import ru.sidey.snake.event.EventHandler;
import ru.sidey.snake.event.EventManager;
import ru.sidey.snake.model.game.Player;
import ru.sidey.snake.model.game.master.event.DeputyChangeEvent;
import ru.sidey.snake.network.event.NodeDeathEvent;
import ru.sidey.snake.network.impl.AddressIdentifier;
import ru.sidey.snake.network.impl.NetworkNode;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class PlayerController {

    private final AtomicInteger playerOrder = new AtomicInteger();

    private MasterPlayer master;

    private NodePlayer deputy;

    private final Collection<NodePlayer> players = new HashSet<>();

    private final Map<AddressIdentifier, NodePlayer> addressPlayers = new HashMap<>();

    public synchronized void initMaster(String name) {
        master = new MasterPlayer(playerOrder.getAndIncrement(), name);
    }

    public synchronized NodePlayer addPlayer(String name, NetworkNode node) {
        NodePlayer pl = new NodePlayer(playerOrder.getAndIncrement(), name, node);
        if (deputy == null) {
            deputy = pl;
            EventManager.runEvent(new DeputyChangeEvent(deputy));
        }
        players.add(pl);
        addressPlayers.put(node.address(), pl);
        return pl;
    }

    public synchronized void removePlayer(Player player) {
        players.removeIf(p -> p.id() == player.id());
        addressPlayers.values().removeIf(p -> p.id() == player.id());
        if (deputy != null && deputy.id() == player.id()) {
            deputy = players.stream().findAny().orElse(null);
            EventManager.runEvent(new DeputyChangeEvent(deputy));
        }
    }

    @EventHandler
    public synchronized void onNodeDeath(NodeDeathEvent e) {
        getPlayer(e.getNode().address()).ifPresent(this::removePlayer);
    }

    public synchronized Optional<NodePlayer> getPlayer(AddressIdentifier address) {
        return Optional.ofNullable(addressPlayers.get(address));
    }

    public synchronized MasterPlayer master() {
        return master;
    }

    public synchronized NodePlayer deputy() {
        return deputy;
    }


    public synchronized Collection<NodePlayer> players() {
        return Collections.unmodifiableCollection(players);
    }

}
