package ru.itis.javalab.exception;

public class AlreadyExistUserException extends RuntimeException {
    public AlreadyExistUserException(String message){
        super(message);
    }
}
