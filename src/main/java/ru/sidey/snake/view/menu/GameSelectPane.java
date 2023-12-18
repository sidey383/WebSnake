package ru.sidey.snake.view.menu;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class GameSelectPane extends VBox {

    public GameSelectPane(GameSelectUnit unit) {
        for (String text : unit.getText()) {
            getChildren().add(new Label(text));
        }
        Button button = new Button("Start");
        button.setOnMouseClicked((e) -> unit.run());
        getChildren().add(button);
    }

}
