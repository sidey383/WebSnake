package ru.sidey.snake.network.event;

import ru.sidey.snake.event.Event;
import ru.sidey.snake.network.ConnectedNode;

public class NodeDeathEvent extends Event {

    private final ConnectedNode node;

    public NodeDeathEvent(ConnectedNode  node) {
        super(true);
        this.node = node;
    }

    public ConnectedNode getNode() {
        return node;
    }
}
