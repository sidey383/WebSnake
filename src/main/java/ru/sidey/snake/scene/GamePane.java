package ru.sidey.snake.scene;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import ru.sidey.snake.game.GameUnit;

public class GamePane extends HBox {

    public GamePane(GameUnit gameUnit) {
        Label textLabel = new Label("Game");
        Button button = new Button("Start");
        button.setOnMouseClicked(e -> System.out.println("Start game!"));
        getChildren().add(textLabel);
        getChildren().add(button);
    }

}
