package ru.rest_demo.exercise_2315.exception_handlers;

public class UserNotCreatedException extends RuntimeException {
    public UserNotCreatedException(String message) { super(message); }
}
