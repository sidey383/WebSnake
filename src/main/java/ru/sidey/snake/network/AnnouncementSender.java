package ru.sidey.snake.network;

import ru.sidey.snake.SnakesProto;

import java.util.function.Supplier;

public interface AnnouncementSender {

    void start(Supplier<SnakesProto.GameMessage.AnnouncementMsg> msgSupplier);

    void stop();

}
