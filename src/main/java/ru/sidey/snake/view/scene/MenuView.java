package ru.sidey.snake.view.scene;

import ru.sidey.snake.view.AppScene;
import ru.sidey.snake.view.menu.GameSelectUnit;

import java.util.Collection;

public abstract class MenuView extends AppScene {

    public abstract void setGameSelectUnits(Collection<GameSelectUnit> games);

}
