package ru.sidey.snake.model.game.master;

import ru.sidey.snake.SnakesProto;
import ru.sidey.snake.model.game.Player;

public record MasterPlayer(int id, String name) implements Player {

    @Override
    public void sendState(SnakesProto.GameState state) {
    }

    @Override
    public void sendError(SnakesProto.GameMessage.ErrorMsg msg) {
    }

    @Override
    public void sendRoleChange(SnakesProto.GameMessage.RoleChangeMsg msg) {
    }
}
