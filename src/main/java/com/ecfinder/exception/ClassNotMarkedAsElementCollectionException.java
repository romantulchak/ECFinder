package com.ecfinder.exception;

public class ClassNotMarkedAsElementCollectionException extends RuntimeException{
    public ClassNotMarkedAsElementCollectionException(Class<?> clazz){
        super(String.format("Class: %s not marked as @ECF", clazz.getSimpleName()));
    }

}
