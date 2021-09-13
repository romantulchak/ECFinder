package com.ecfinder.exception;

public class TotalElementsCountNullException extends RuntimeException{

    public TotalElementsCountNullException(long id){
        super(String.format("Total elements are null for id %s", id));
    }

}
