package ru.sidey.snake.view.scene;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import ru.sidey.snake.view.menu.GameSelectPane;
import ru.sidey.snake.view.menu.GameSelectUnit;

import java.util.Collection;

public class MenuAppScene extends MenuView {

    @FXML
    public VBox scrollBox;

    @Override
    public void setGameSelectUnits(Collection<GameSelectUnit> games) {
        Platform.runLater(() -> {
                    scrollBox.getChildren().clear();
                    scrollBox.getChildren().addAll(games.stream().map(GameSelectPane::new).toList());
                }
        );
    }


    @FXML
    public void pressNewGame(ActionEvent actionEvent) {
    }
}
