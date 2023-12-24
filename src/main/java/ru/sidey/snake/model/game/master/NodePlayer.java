package ru.sidey.snake.model.game.master;

import ru.sidey.snake.SnakesProto;
import ru.sidey.snake.model.game.Player;
import ru.sidey.snake.network.impl.NetworkNode;

import java.util.concurrent.atomic.AtomicLong;

public record NodePlayer(int id, String name, NetworkNode node, AtomicLong steer) implements Player {

    public NodePlayer(int id, String name, NetworkNode node) {
        this(id, name, node, new AtomicLong(-1));
    }

    @Override
    public void sendState(SnakesProto.GameState state) {
        node.sendPacket(
                SnakesProto.GameMessage.newBuilder()
                        .setState(
                                SnakesProto.GameMessage.StateMsg.newBuilder()
                                        .setState(state)
                                        .build()
                        )
                        .build());
    }

    @Override
    public void sendError(SnakesProto.GameMessage.ErrorMsg msg) {
        node.sendPacket(SnakesProto.GameMessage.newBuilder().setError(msg).build());
    }

    @Override
    public void sendRoleChange(SnakesProto.GameMessage.RoleChangeMsg msg) {
        node.sendPacket(SnakesProto.GameMessage.newBuilder().setRoleChange(msg).build());
    }

    public synchronized boolean applySteer(long seq) {
        if (steer.get() < seq) {
            steer.set(seq);
            return true;
        }
        return false;
    }

}
