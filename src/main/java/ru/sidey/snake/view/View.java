package ru.sidey.snake.view;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.jetbrains.annotations.Nullable;
import ru.sidey.snake.event.EventHandler;
import ru.sidey.snake.event.EventManager;
import ru.sidey.snake.view.event.PlayerKeyEvent;
import ru.sidey.snake.view.event.WindowCloseEvent;
import ru.sidey.snake.view.factory.MenuSceneFactory;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;

public class View implements ViewInterface {

    private final Stage stage;

    List<SceneFactory<? extends AppScene>> sceneFactories = List.of(
            new MenuSceneFactory()
    );

    public View(Stage stage) {
        this.stage = stage;
        this.stage.setMaximized(true);
        stage.setOnCloseRequest(this::windowsClose);
        stage.setFullScreenExitHint("");
        stage.setResizable(true);
        stage.setTitle("Piano tiles");
        try {
            stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResource("/images/icon.png")).toURI().toString()));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        EventManager.registerListener(this);
    }

    private void windowsClose(WindowEvent windowEvent) {
        EventManager.runEvent(new WindowCloseEvent());
    }

    @Override
    public void setScene(AppScene controller) {
        Platform.runLater(() -> {
            if (stage.getScene() == null) {
                stage.setScene(new Scene(controller.content()));
            } else {
                stage.getScene().setRoot(controller.content());
            }
            if (!stage.isShowing()) {
                stage.show();
            }
        });

    }

    @EventHandler
    public void onKeyPress(PlayerKeyEvent keyEvent) {
        if (keyEvent.isPress() && keyEvent.keyCode() == KeyCode.F11) {
            stage.setFullScreen(!stage.fullScreenProperty().get());
        }
    }

    @Nullable
    @SuppressWarnings("unchecked")
    public <T extends AppScene> T getScene(Class<T> clazz) throws IOException {
        for (SceneFactory<? extends AppScene> factory : sceneFactories) {
            Class<?> factoryClazz = (Class<?>) ((ParameterizedType) factory.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
            if (clazz.isAssignableFrom(factoryClazz)) {
                return (T) factory.createScene();
            }
        }
        return null;
    }

    @Override
    public void close() {
        EventManager.unregisterListener(this);
        Platform.runLater(stage::close);
    }

}
