package ru.sidey.snake.model;

import ru.sidey.snake.SnakesProto;
import ru.sidey.snake.event.EventHandler;
import ru.sidey.snake.event.EventManager;
import ru.sidey.snake.model.event.GameListUpdateEvent;
import ru.sidey.snake.network.impl.AnnouncementNetworkReceiver;
import ru.sidey.snake.network.AnnouncementReceiver;
import ru.sidey.snake.network.event.GameAnnouncementEvent;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class GameListener extends TimerTask {

    Set<SnakesProto.GameAnnouncement> msgs = new HashSet<>();

    private final AnnouncementReceiver receiver;

    private Timer timer;

    public GameListener() {
        try {
            receiver = new AnnouncementNetworkReceiver(InetAddress.getByName("239.192.0.4"), 9192);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    public void start() {
        EventManager.registerListener(this);
        synchronized (this) {
            msgs.clear();
            if (timer != null)
                timer.cancel();
            timer = new Timer();
            timer.schedule(this, 0, 1500);
            receiver.start();
        }
    }

    public void stop() {
        synchronized (this) {
            receiver.stop();
            if (timer != null) {
                timer.cancel();
                timer = null;
            }
        }
        EventManager.unregisterListener(this);
    }

    @EventHandler
    public void onAnnouncementMessage(GameAnnouncementEvent e) {
        synchronized (this) {
            msgs.addAll(e.getAnnouncement().getGamesList());
        }
    }

    public void run() {
        synchronized (this) {
            EventManager.runEvent(new GameListUpdateEvent(msgs));
            msgs = new HashSet<>();
        }
    }


}
