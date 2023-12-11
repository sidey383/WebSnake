module ru.sidey.snake {
    requires javafx.controls;
    requires javafx.fxml;
    requires protobuf.java;
    requires org.apache.logging.log4j;
    exports ru.sidey.snake;
    opens ru.sidey.snake.scene;
}
