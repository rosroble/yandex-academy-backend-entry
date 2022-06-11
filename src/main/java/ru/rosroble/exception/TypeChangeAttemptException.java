package ru.rosroble.exception;

public class TypeChangeAttemptException extends ValidationException{
    public TypeChangeAttemptException(String message) {
        super(message);
    }
}
