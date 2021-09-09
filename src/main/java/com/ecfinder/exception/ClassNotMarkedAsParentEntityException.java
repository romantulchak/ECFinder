package com.ecfinder.exception;

public class ClassNotMarkedAsParentEntityException extends RuntimeException{
    public ClassNotMarkedAsParentEntityException(Class<?> clazz){
        super(String.format("Class %s not marked as @ECFEntity", clazz.getSimpleName()));
    }
}
