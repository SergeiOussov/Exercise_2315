package ru.rest_demo.exercise_2315.exception_handlers;

public class UserNotUpdatedException extends RuntimeException {
    public UserNotUpdatedException(String message) {
        super(message);
    }
}
