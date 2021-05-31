package ru.itis.javalab.exception;

public class AlreadyExistEventException extends RuntimeException {
    public AlreadyExistEventException(String message){
        super(message);
    }
}
