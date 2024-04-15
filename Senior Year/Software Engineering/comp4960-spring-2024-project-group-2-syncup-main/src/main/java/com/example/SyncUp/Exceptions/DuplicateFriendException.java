package com.example.SyncUp.Exceptions;

public class DuplicateFriendException extends Exception {
    public DuplicateFriendException() {
        super();
    }

    public DuplicateFriendException(String message) {
        super(message);
    }

    public DuplicateFriendException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateFriendException(Throwable cause) {
        super(cause);
    }
}