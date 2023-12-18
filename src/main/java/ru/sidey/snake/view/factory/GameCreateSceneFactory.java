package ru.sidey.snake.view.factory;

import ru.sidey.snake.view.SceneFactory;
import ru.sidey.snake.view.scene.GameCreateAppScene;
import ru.sidey.snake.view.scene.GameCreateView;

import java.net.URL;

public class GameCreateSceneFactory extends SceneFactory<GameCreateView> {
    @Override
    protected URL getFXMLPath() {
        return getClass().getResource("/fxml/GameCreateScene.fxml");
    }

    @Override
    protected Object controllerFXMLFactory(Class<?> clazz) {
        return new GameCreateAppScene();
    }
}
