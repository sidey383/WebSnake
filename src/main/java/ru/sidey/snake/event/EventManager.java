package ru.sidey.snake.event;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventManager {

    private static final Logger logger = LogManager.getLogger(EventManager.class);

    private final static Map<Class<? extends Event>, List<EventExecutor>> executorMap = new HashMap<>();

    private EventManager() {
    }

    @SuppressWarnings("unchecked")
    public static void registerListener(Object listener) {
        if (listener == null)
            return;
        Class<?> clazz = listener.getClass();
        for (Method method : clazz.getDeclaredMethods()) {
            if (Modifier.isStatic(method.getModifiers()))
                continue;
            if (!method.canAccess(listener))
                continue;
            EventHandler eventHandler = method.getAnnotation(EventHandler.class);
            if (eventHandler == null)
                continue;
            Class<?>[] params = method.getParameterTypes();
            if (params.length != 1 || !Event.class.isAssignableFrom(params[0]))
                return;
            synchronized (executorMap) {
                if (!executorMap.containsKey(params[0]))
                    executorMap.put((Class<? extends Event>) params[0], new ArrayList<>());
                executorMap.get(params[0]).add(new EventExecutor(listener, method));
            }
        }
    }

    public static void unregisterListener(Object listener) {
        synchronized (executorMap) {
            executorMap.values().forEach(
                    list -> list.removeIf(
                            executor -> executor.getObject().equals(listener)
                    )
            );
        }
    }

    public static void runEvent(Event event) {
        Class<? extends Event> clazz = event.getClass();
        List<EventExecutor> originExecutors;
        synchronized (executorMap) {
            originExecutors = executorMap.get(clazz);
        }
        if (originExecutors == null)
            return;
        List<EventExecutor> executors = new ArrayList<>(originExecutors);
        if (event.isAsynchronous()) {
            executors.forEach(e ->
                    new Thread(() -> {
                        try {
                            e.execute(event);
                        } catch (Throwable ex) {
                            logger.error("Asynchronous event run exception", ex);
                        }
                    }).start());
        } else {
            executors.forEach(e -> {
                try {
                    e.execute(event);
                } catch (Throwable ex) {
                    logger.error("Synchronous event run exception", ex);
                }
            });
        }
    }

}
