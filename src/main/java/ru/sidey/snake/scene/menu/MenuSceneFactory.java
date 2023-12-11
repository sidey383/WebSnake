package ru.sidey.snake.scene.menu;

import ru.sidey.snake.scene.MenuScene;
import ru.sidey.snake.scene.SceneFactory;

import java.net.URL;

public class MenuSceneFactory extends SceneFactory<MenuScene> {
    @Override
    protected URL getFXMLPath() {
        return getClass().getResource("/fxml/MenuScene.fxml");
    }

    @Override
    protected Object controllerFXMLFactory(Class<?> clazz) {
        return new MenuScene();
    }
}
