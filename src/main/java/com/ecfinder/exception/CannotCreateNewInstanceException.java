package com.ecfinder.exception;

public class CannotCreateNewInstanceException extends RuntimeException{

    public CannotCreateNewInstanceException(Class<?> clazz){
        super(String.format("Cannot create instance of %s", clazz.getSimpleName()));
    }
}
