package ru.sidey.snake;

import javafx.application.Application;
import javafx.stage.Stage;
import ru.sidey.snake.scene.AppScene;
import ru.sidey.snake.scene.MenuScene;
import ru.sidey.snake.scene.menu.MenuSceneFactory;

import java.io.IOException;

import static ru.sidey.snake.SnakesProto.GameState.Snake.*;
import static ru.sidey.snake.SnakesProto.GameState.*;
import static ru.sidey.snake.SnakesProto.*;

/**
 * JavaFX App
 */
public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        View view = new View(stage);
        MenuSceneFactory factory = new MenuSceneFactory();
        AppScene scene = factory.createScene();
        view.setScene(scene);
        view.getStage().show();
        Snake snake = Snake.newBuilder()
                .setPlayerId(1)
                .setHeadDirection(Direction.LEFT)
                .setState(SnakeState.ALIVE)
                .addPoints(coord(5, 1)) // голова
                .addPoints(coord(3, 0))
                .addPoints(coord(0, 2))
                .addPoints(coord(-4, 0))
                .build();
        System.out.println(snake.toString());
    }

    private GameState.Coord coord(int x, int y) {
        return GameState.Coord.newBuilder().setX(x).setY(y).build();
    }


    public static void main(String[] args) {
        launch();
    }

}