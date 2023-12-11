package ru.sidey.snake.scene;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import ru.sidey.snake.game.GameUnit;

import java.util.Collection;

public class MenuScene extends AppScene {

    @FXML
    public VBox scrollBox;

    public void setGames(Collection<GameUnit> choices) {
        Platform.runLater(() -> {
                    scrollBox.getChildren().clear();
                    scrollBox.getChildren().addAll(choices.stream().map(GamePane::new).toList());
                }
        );
    }


    @FXML
    public void pressMenu(ActionEvent actionEvent) {
    }

}
