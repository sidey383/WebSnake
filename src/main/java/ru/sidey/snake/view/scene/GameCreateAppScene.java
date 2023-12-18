package ru.sidey.snake.view.scene;

import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class GameCreateAppScene extends GameCreateView {
    public TextField nameField;
    public TextField foodField;
    public TextField stateDelay;
    public TextField widthField;
    public TextField heightField;
    public Label message;

    public void startGame(ActionEvent actionEvent) {
        String name;
        int width;
        int height;
        int food;
        name = nameField.getText();
        if (name.isEmpty()) {
            message.setText("Name can't be empty");
            return;
        }
        try {
            width = Integer.parseInt(widthField.getText());
            height = Integer.parseInt(heightField.getText());
            food = Integer.parseInt(foodField.getText());
        } catch (NumberFormatException e) {
            message.setText("Size or food not a number");
            return;
        }
        if (height < 10 || height > 100 || width < 10 || width > 100) {
            message.setText("Wrong field size");
            return;
        }
        if (food < 0 || food > 100) {
            message.setText("Wrong food value");
            return;
        }

    }
}
