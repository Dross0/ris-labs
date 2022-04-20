package ru.gaidamaka.exception;

public class SchemeException extends RuntimeException{
    public SchemeException() {
        super();
    }

    public SchemeException(String message) {
        super(message);
    }

    public SchemeException(String message, Throwable cause) {
        super(message, cause);
    }

    public SchemeException(Throwable cause) {
        super(cause);
    }
}
