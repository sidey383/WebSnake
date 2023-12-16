package ru.sidey.snake.view.factory;

import ru.sidey.snake.view.SceneFactory;
import ru.sidey.snake.view.scene.MenuAppScene;
import ru.sidey.snake.view.scene.MenuView;

import java.net.URL;

public class MenuSceneFactory extends SceneFactory<MenuView> {
    @Override
    protected URL getFXMLPath() {
        return getClass().getResource("/fxml/MenuScene.fxml");
    }

    @Override
    protected Object controllerFXMLFactory(Class<?> clazz) {
        return new MenuAppScene();
    }
}
