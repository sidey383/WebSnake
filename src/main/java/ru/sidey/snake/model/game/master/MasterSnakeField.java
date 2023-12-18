package ru.sidey.snake.model.game.master;

import ru.sidey.snake.model.game.BaseSnakeField;
import ru.sidey.snake.model.game.FieldSize;
import ru.sidey.snake.model.game.object.Direction;
import ru.sidey.snake.model.game.object.Pose;
import ru.sidey.snake.model.game.object.SimpleSnake;
import ru.sidey.snake.model.game.object.Snake;
import ru.sidey.snake.model.game.player.Player;

import java.util.*;
import java.util.stream.Collectors;

public class MasterSnakeField extends BaseSnakeField {

    private static final int snakePlaceSize = 5;

    private final Random rand = new Random();

    private List<SnakePlace> newSnakePlace;

    public MasterSnakeField(int order, FieldSize size, int staticFood, Collection<Snake> snakes, Collection<Pose> food) {
        super(order, size, staticFood, snakes, food);
        Collection<Pose> snakeUse = snakes.stream().flatMap(s -> s.body().stream()).toList();
        List<SnakePlace> newSnakePlace = new ArrayList<>();
        for (int i = 0; i < size.width(); i++) {
            for (int j = 0; j < size.height(); j++) {
                canPlace(i, j, snakeUse, food).ifPresent(newSnakePlace::add);
            }
        }
        this.newSnakePlace = newSnakePlace;
    }

    private Optional<SnakePlace> canPlace(int x, int y, Collection<Pose> snakeUse, Collection<Pose> foodUse) {
        for (int i = 0; i < snakePlaceSize; i++) {
            for (int j = 0; j < snakePlaceSize; j++) {
                Pose p = new Pose(x + i, y + j, size);
                if (snakeUse.contains(p))
                    return Optional.empty();
            }
        }
        Pose head = new Pose(x + snakePlaceSize / 2, y + snakePlaceSize / 2, size);
        if (foodUse.contains(head)) {
            return Optional.empty();
        }
        for (Direction dir : Direction.values()) {
            Pose tail = head.add(dir);
            if (!foodUse.contains(tail)) {
                return Optional.of(new SnakePlace(head, tail, dir.opposite()));
            }
        }
        return Optional.empty();
    }

    public MasterSnakeField(MasterSnakeField field, Collection<Snake> snakes, Collection<Pose> food) {
        super(field.order + 1, field.size, field.staticFood, snakes, food);
    }

    public synchronized MasterSnakeField nextStep() {
        List<MoveableSnake> snakes = getSnakes().stream()
                .map(MoveableSnake::new)
                .collect(Collectors.toCollection(ArrayList::new));
        Collection<Pose> food = new HashSet<>(getFood());
        snakes.forEach(MoveableSnake::move);
        applyFood(snakes, food);
        Collection<MoveableSnake> dead = checkCollisions(snakes);
        snakes.removeAll(dead);
        List<Pose> availablePose = new ArrayList<>(size.total());
        for (int i = 0; i < size().width(); i++) {
            for (int j = 0; j < size().height(); j++) {
                availablePose.add(new Pose(i, j, size));
            }
        }
        snakes.forEach(s -> availablePose.removeAll(s.body()));
        availablePose.removeAll(food);
        addFood(availablePose, food, staticFood + snakes.size());
        deathFood(availablePose, food, dead);
        return new MasterSnakeField(this, new ArrayList<>(snakes), food);
    }

    private <T extends MoveableSnake> Collection<T> checkCollisions(List<T> snakes) {
        Collection<T> dead = new HashSet<>();
        for (int i = 0; i < snakes.size(); i++) {
            Pose head = snakes.get(i).getHeade();
            for (int j = 0; j < snakes.size(); j++) {
                if (snakes.get(j).body().contains(head)) {
                    snakes.get(j).addScore();
                    dead.add(snakes.get(j));
                }
            }
        }
        return dead;
    }

    private void deathFood(Collection<Pose> available, Collection<Pose> food, Collection<MoveableSnake> deadSnakes) {
        for (var s : deadSnakes) {
            for (Pose p : s.body()) {
                if (available.contains(p) && rand.nextInt() % 2 == 0) {
                    available.remove(p);
                    food.add(p);
                }
            }
        }
    }

    private void addFood(List<Pose> available, Collection<Pose> food, int required) {
        Collections.shuffle(available);
        while (food.size() < required && !available.isEmpty()) {
            food.add(available.remove(0));
        }
    }

    public synchronized Optional<Snake> createSnake(Player player) {
        if (newSnakePlace.isEmpty())
            return Optional.empty();
        Collections.shuffle(newSnakePlace);
        SnakePlace place = newSnakePlace.get(0);
        Snake sn = new SimpleSnake(player.id(), List.of(place.head(), place.tail()), player, place.direction());
        newSnakePlace.removeIf(r ->
                Math.floorMod(r.head().x() - place.head().x(), place.head().size().height()) < snakePlaceSize / 2 ||
                Math.floorMod(r.head().y() - place.head().y(), place.head().size().width()) < snakePlaceSize / 2 ||
                Math.floorMod(r.tail().x() - place.head().x(), place.head().size().height()) < snakePlaceSize / 2 ||
                Math.floorMod(r.tail().y() - place.head().y(), place.head().size().width()) < snakePlaceSize / 2 ||
                Math.floorMod(r.head().x() - place.tail().x(), place.head().size().height()) < snakePlaceSize / 2 ||
                Math.floorMod(r.head().y() - place.tail().y(), place.head().size().width()) < snakePlaceSize / 2 ||
                Math.floorMod(r.tail().x() - place.tail().x(), place.head().size().height()) < snakePlaceSize / 2 ||
                Math.floorMod(r.tail().y() - place.tail().y(), place.head().size().width()) < snakePlaceSize / 2

        );
        return Optional.of(sn);
    }

    public synchronized boolean canCreateSnake() {
        return !newSnakePlace.isEmpty();
    }

    public synchronized void replaceSnake(Snake snake) {
        snakes.removeIf(s -> s.id() == snake.id());
        snakes.add(snake);
    }

    private void applyFood(Collection<MoveableSnake> snakes, Collection<Pose> food) {
        for (var s : snakes) {
            Pose head = s.getHeade();
            if (food.contains(head)) {
                food.remove(head);
                s.addScore();
            } else {
                s.removeTail();
            }
        }
    }

}
