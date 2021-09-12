package com.ecfinder.exception;

public class TableNotExistsException extends RuntimeException {
    public TableNotExistsException(String tableName){
        super(String.format("Table name %s: does not exist", tableName));
    }
}
