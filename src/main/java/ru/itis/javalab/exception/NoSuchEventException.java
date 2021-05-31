package ru.itis.javalab.exception;


import lombok.NoArgsConstructor;

@NoArgsConstructor
public class NoSuchEventException extends RuntimeException {
    public NoSuchEventException(String message){
        super(message);
    }
}
