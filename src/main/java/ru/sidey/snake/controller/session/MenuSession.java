package ru.sidey.snake.controller.session;

import ru.sidey.snake.SnakesProto;
import ru.sidey.snake.controller.Controller;
import ru.sidey.snake.controller.ControllerSession;
import ru.sidey.snake.controller.event.GameSelectEvent;
import ru.sidey.snake.controller.exception.ControllerException;
import ru.sidey.snake.event.EventHandler;
import ru.sidey.snake.event.EventManager;
import ru.sidey.snake.model.event.GameListUpdateEvent;
import ru.sidey.snake.view.AppScene;
import ru.sidey.snake.view.menu.GameSelectUnit;
import ru.sidey.snake.view.scene.MenuView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MenuSession extends ControllerSession {

    private final MenuView view;

    public MenuSession(MenuView view) {
        this.view = view;
    }

    @Override
    protected void start(Controller controller) throws ControllerException {
        super.start(controller);
        controller().model().startListenGames();
    }

    @EventHandler
    public void onGameListUpdate(GameListUpdateEvent e) {
        view.setGameSelectUnits(
                e.getAnnouncements()
                        .stream()
                        .map(GameRecord::new)
                        .collect(Collectors.toList())
        );
    }

    @Override
    protected void stop() throws ControllerException {
        super.stop();
        controller().model().stopListenGames();
    }

    @Override
    public AppScene scene() {
        return view;
    }

    private record GameRecord(SnakesProto.GameAnnouncement msg) implements GameSelectUnit {

        @Override
        public List<String> getText() {
            List<String> lore = new ArrayList<>();
            lore.add("Game name: " + msg.getGameName());
            lore.add("Can join: " + msg.getCanJoin());
            lore.add("Field: " + msg.getConfig().getWidth() + "x" + msg.getConfig().getHeight());
            lore.add("Player count:" + msg.getPlayers().getPlayersCount());
            lore.add("Food: " + msg.getConfig().getFoodStatic() + "+" + msg.getPlayers().getPlayersCount());
            return lore;
        }

        @Override
        public void run() {
            EventManager.runEvent(new GameSelectEvent(msg));
        }
    }

}
