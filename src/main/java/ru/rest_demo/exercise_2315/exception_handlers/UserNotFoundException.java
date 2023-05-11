package ru.rest_demo.exercise_2315.exception_handlers;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) { super(message); }
}
