package ru.sidey.snake.view.event;

import javafx.scene.input.KeyCode;
import ru.sidey.snake.event.Event;

public class PlayerKeyEvent extends Event {

    private final KeyCode keyCode;

    private final boolean isPress;

    private final long createTime;

    public PlayerKeyEvent(boolean isPress, KeyCode keyCode) {
        super(false);
        this.keyCode = keyCode;
        this.isPress = isPress;
        createTime = System.nanoTime();
    }

    public KeyCode keyCode() {
        return keyCode;
    }

    public boolean isPress() {
        return isPress;
    }

    public long createNanoTine() {
        return createTime;
    }

}
