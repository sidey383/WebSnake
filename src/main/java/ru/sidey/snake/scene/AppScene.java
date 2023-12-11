package ru.sidey.snake.scene;

import javafx.scene.Parent;
import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import ru.sidey.snake.View;

public abstract class AppScene {

    @FXML
    protected Parent root;

    private View view;

    public Parent content() {
        return root;
    }

    public void setView(View view) {
        this.view = view;
    }

    @FXML
    public void onKeyPress(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.F11) {
            Stage stage = view.getStage();
            stage.setFullScreen(!stage.fullScreenProperty().get());
        }
    }

    @FXML
    public void onKeyRelease(KeyEvent keyEvent) {
    }

}
