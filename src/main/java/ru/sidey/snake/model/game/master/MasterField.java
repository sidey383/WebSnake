package ru.sidey.snake.model.game.master;

import ru.sidey.snake.model.game.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class MasterField implements GameField {

    private static final int SNAKE_SPACE = 5;

    private final Random random = new Random();

    private final AtomicInteger stateOrder = new AtomicInteger();

    private final FieldConfig config;

    private final List<MasterSnake> snakes = new ArrayList<>();

    private List<SnakePlace> availablePoses = new ArrayList<>();

    private final Collection<Pose> food = new HashSet<>();

    public MasterField(FieldConfig config) {
        this.config = config;
        fixFood();
        availablePoses = shuffleSnakePlaces();
    }

    public boolean hasPlace() {
        return !availablePoses.isEmpty();
    }

    public synchronized Optional<Snake> createSnake(Player owner) {
        if (availablePoses.isEmpty())
            return Optional.empty();
        Collections.shuffle(availablePoses);
        SnakePlace place = availablePoses.get(0);
        MasterSnake snake = new MasterSnake(owner.id(), place.head, place.direction);
        Pose bodyPose = place.head.applyDirection(place.direction.opposite());
        snakes.add(snake);
        availablePoses.removeIf(p -> p.head().minDistance(place.head) <= 2 || p.head().minDistance(bodyPose) <= 2);
        return Optional.of(snake);
    }

    public synchronized void nextStep() {
        snakes.forEach(MasterSnake::moveBody);
        snakes.forEach(s -> {
            if (food.contains(s.head())) {
                food.remove(s.head());
                s.incrementScore();
            } else {
                s.removeTail();
            }
        });
        Collection<MasterSnake> deathSnake = checkCollisions();
        deathSnake.forEach(this::addSnakeFood);
        fixFood();
        availablePoses = shuffleSnakePlaces();
        stateOrder.incrementAndGet();
    }

    private Collection<MasterSnake> checkCollisions() {
        Collection<MasterSnake> deathSnake = new HashSet<>();
        for (MasterSnake toCheck : snakes) {
            for (MasterSnake snake : snakes) {
                if (snake.body().contains(toCheck.head())) {
                    deathSnake.add(toCheck);
                    snake.incrementScore();
                }
            }
        }
        snakes.removeAll(deathSnake);
        return deathSnake;
    }

    private List<SnakePlace> shuffleSnakePlaces() {
        Collection<Pose> snakePoses = snakes.stream().flatMap(s -> s.body().stream()).toList();
        List<SnakePlace> places = new ArrayList<>();
        for (int x = 0; x < config.width(); x++) {
            for (int y = 0; y < config.height(); y++) {
                Pose p = new Pose(x, y, config);
                boolean hasePlace = true;
                for (int i = -2;  i < SNAKE_SPACE / 2; i++) {
                    for (int j = -2;  j < SNAKE_SPACE / 2; j++) {
                        if (snakePoses.contains(p.apply(i, j))) {
                            hasePlace = false;
                        }
                    }
                }
                if (hasePlace) {
                    for (Direction dir : Direction.values()) {
                        if (!food.contains(p.applyDirection(dir.opposite()))) {
                            places.add(new SnakePlace(p, dir));
                        }
                    }
                }
            }
        }
        Collections.shuffle(places);
        return places;
    }

    private void addSnakeFood(Snake snake) {
        Collection<Pose> usePoses = new ArrayList<>();
        usePoses.addAll(food);
        usePoses.addAll(snakes.stream().flatMap(s -> s.body().stream()).toList());
        for (Pose p : snake.body()) {
            if (!usePoses.contains(p) && random.nextInt() % 2 == 0) {
                usePoses.add(p);
                food.add(p);
            }
        }
    }

    private void fixFood() {
        int requiredFood = food.size()- (config.foodStatic() + snakes.size());
        if (requiredFood <= 0)
            return;
        List<Pose> available = new ArrayList<>();
        for (int x = 0; x < config.width(); x++) {
            for (int y = 0; y < config.height(); y++) {
                available.add(new Pose(x, y, config));
            }
        }
        available.removeAll(food);
        available.removeAll(snakes.stream().flatMap(s -> s.body().stream()).toList());
        Collections.shuffle(available);
        food.addAll(available.stream().limit(requiredFood).toList());
    }

    public synchronized int stateOrder() {
        return stateOrder.get();
    }

    @Override
    public synchronized FieldConfig config() {
        return config;
    }

    @Override
    public synchronized Collection<MasterSnake> snakes() {
        return Collections.unmodifiableCollection(snakes);
    }

    @Override
    public synchronized Collection<Pose> food() {
        return Collections.unmodifiableCollection(food);
    }

    private record SnakePlace(
            Pose head,
            Direction direction
    ) {}

}
