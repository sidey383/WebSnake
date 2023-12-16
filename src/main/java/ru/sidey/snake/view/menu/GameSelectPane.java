package ru.sidey.snake.view.menu;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class GameSelectPane extends HBox {

    public GameSelectPane(GameSelectUnit unit) {
        Label textLabel = new Label(unit.getText());
        Button button = new Button("Start");
        button.setOnMouseClicked((e) -> unit.run());
        getChildren().add(textLabel);
        getChildren().add(button);
    }

}
