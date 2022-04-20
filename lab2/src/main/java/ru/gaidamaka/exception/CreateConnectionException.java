package ru.gaidamaka.exception;

public class CreateConnectionException extends RuntimeException{
    public CreateConnectionException() {
    }

    public CreateConnectionException(String message) {
        super(message);
    }

    public CreateConnectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public CreateConnectionException(Throwable cause) {
        super(cause);
    }
}
