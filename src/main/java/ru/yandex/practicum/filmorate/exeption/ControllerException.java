package ru.yandex.practicum.filmorate.exeption;

public class ControllerException extends RuntimeException {

    public ControllerException(String message) {
        super(message);
    }
}