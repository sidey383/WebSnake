package ru.sidey.snake;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import ru.sidey.snake.scene.AppScene;

public class View {

    private final Stage stage;

    public View(Stage stage) {
        this.stage = stage;
        this.stage.setMaximized(true);
        stage.setOnCloseRequest(this::windowsClose);
        stage.setFullScreenExitHint("");
        stage.setResizable(true);
        stage.setTitle("Snake");
        stage.getIcons().add(new Image("/icon.png"));
    }

    public void setScene(AppScene scene) {
        Platform.runLater(() -> {
            scene.setView(this);
            if (stage.getScene() == null) {
                stage.setScene(new Scene(scene.content()));
            } else {
                stage.getScene().setRoot(scene.content());
            }
            if (!stage.isShowing()) {
                stage.show();
            }
        });
    }

    public Stage getStage() {
        return stage;
    }

    private void windowsClose(WindowEvent windowEvent) {
        //TODO: add realization
    }

}
