package ru.itis.javalab.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class NoFreeTimeException extends RuntimeException {
    public NoFreeTimeException(String message){
        super(message);
    }
}
