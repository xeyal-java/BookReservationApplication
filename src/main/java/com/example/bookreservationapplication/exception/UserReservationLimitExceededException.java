package com.example.bookreservationapplication.exception;

public class UserReservationLimitExceededException extends RuntimeException {
    public UserReservationLimitExceededException(String message) {
        super(message);
    }
}
