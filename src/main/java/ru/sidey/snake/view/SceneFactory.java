package ru.sidey.snake.view;

import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.net.URL;

public abstract class SceneFactory<T extends AppScene> {

    public T createScene() throws IOException {
        FXMLLoader loader = new FXMLLoader(getFXMLPath());
        loader.setControllerFactory(this::controllerFXMLFactory);
        loader.load();
        return loader.getController();
    }

    protected abstract URL getFXMLPath();

    protected abstract Object controllerFXMLFactory(Class<?> clazz);


}
