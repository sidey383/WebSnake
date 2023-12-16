package ru.sidey.snake;

import javafx.application.Application;
import javafx.stage.Stage;
import ru.sidey.snake.view.View;
import ru.sidey.snake.view.scene.MenuView;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException, InterruptedException {
        View view = new View(stage);
        MenuView menuView = view.getScene(MenuView.class);
        view.setScene(menuView);
    }

    public static void main(String[] args) {
        launch();
    }

}