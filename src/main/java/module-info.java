module ru.sidey.snake {
    requires javafx.controls;
    requires javafx.fxml;
    requires protobuf.java;
    requires org.apache.logging.log4j;
    requires org.jetbrains.annotations;
    exports ru.sidey.snake;
    opens ru.sidey.snake.view;
    opens ru.sidey.snake.view.scene;
}
