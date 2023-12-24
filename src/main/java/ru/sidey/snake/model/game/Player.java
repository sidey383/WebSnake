package ru.sidey.snake.model.game;

import ru.sidey.snake.SnakesProto;

public interface Player {

    int id();

    String name();

    void sendState(SnakesProto.GameState state);

    void sendError(SnakesProto.GameMessage.ErrorMsg msg);

    void sendRoleChange(SnakesProto.GameMessage.RoleChangeMsg msg);

}
