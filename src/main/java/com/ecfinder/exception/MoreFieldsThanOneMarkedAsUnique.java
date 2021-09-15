package com.ecfinder.exception;

public class MoreFieldsThanOneMarkedAsUnique extends RuntimeException {
    public MoreFieldsThanOneMarkedAsUnique(String fields) {
        super(String.format("More fields than one are marked as unique: %s", fields));
    }
}
