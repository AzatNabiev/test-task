package ru.itis.javalab.exception;

public class IncorrectGivenData extends RuntimeException {
    public IncorrectGivenData(String message){
        super(message);
    }
}
