package com.ecfinder.core.utils;

import com.ecfinder.exception.CannotCreateNewInstanceException;
import java.lang.reflect.InvocationTargetException;

public final class EntityInstanceUtils<E> {

    @SuppressWarnings("unchecked")
    public E getNewInstance(Class<?> clazz){
        try {
            return (E) clazz.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new CannotCreateNewInstanceException(clazz);
        }
    }

}
