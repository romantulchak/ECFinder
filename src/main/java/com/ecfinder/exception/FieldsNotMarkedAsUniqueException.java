package com.ecfinder.exception;

public class FieldsNotMarkedAsUniqueException extends RuntimeException {
    public FieldsNotMarkedAsUniqueException(Class<?> clazz) {
        super(String.format("No fields in class: %s not marked as @ECFUnique", clazz.getSimpleName()));
    }
}
