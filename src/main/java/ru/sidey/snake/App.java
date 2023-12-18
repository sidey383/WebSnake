package ru.sidey.snake;

import javafx.application.Application;
import javafx.stage.Stage;
import ru.sidey.snake.controller.Controller;
import ru.sidey.snake.controller.session.creator.GameCreateSessionCreator;
import ru.sidey.snake.controller.session.creator.MenuSessionCreator;
import ru.sidey.snake.event.EventManager;
import ru.sidey.snake.model.GameHolder;
import ru.sidey.snake.model.Model;
import ru.sidey.snake.model.ModelInterface;
import ru.sidey.snake.view.View;
import ru.sidey.snake.view.ViewInterface;
import ru.sidey.snake.view.event.MenuOpenEvent;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException, InterruptedException {
        ViewInterface view = new View(stage);
        ModelInterface model = new Model();
        Controller controller = new Controller(view, model);
        controller.addSessionCreator(new MenuSessionCreator());
        controller.addSessionCreator(new GameCreateSessionCreator());
        EventManager.runEvent(new MenuOpenEvent());
        GameHolder holder1 = new GameHolder("Test1",    "sidey383", 300, 300, 100, 200);
        GameHolder holder2 = new GameHolder("Test2", "sidey383", 100, 300, 200, 150);
        GameHolder holder3 = new GameHolder("Test3", "sidey383", 400, 300, 200, 150);
        holder1.start();
        holder2.start();
        holder3.start();
        Thread.sleep(5000);
        holder1.stop();
        holder2.stop();
        holder3.stop();
    }

    public static void main(String[] args) {
        launch();
    }

}