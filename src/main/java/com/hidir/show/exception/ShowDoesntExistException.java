package com.hidir.show.exception;

public class ShowDoesntExistException extends RuntimeException {

    public ShowDoesntExistException(String message) {
        super(message);
    }
}
