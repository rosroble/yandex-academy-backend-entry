package ru.rosroble.exception;

public class BadPriceException extends ValidationException{
    public BadPriceException(String message) {
        super(message);
    }
}
