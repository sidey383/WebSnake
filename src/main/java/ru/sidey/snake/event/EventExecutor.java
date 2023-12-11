package ru.sidey.snake.event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public record EventExecutor(Object object, Method method) {

    public void execute(Event e) throws InvocationTargetException, IllegalAccessException {
        method.invoke(object, e);
    }

    public Object getObject() {
        return object;
    }


}
