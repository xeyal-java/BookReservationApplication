package com.example.bookreservationapplication.controller;

import com.example.bookreservationapplication.exception.*;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BookNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ErrorResponse handleException(BookNotFoundException e) {
      return new ErrorResponse("Book.Not.Found", e.getMessage());
    }

    @ExceptionHandler(BookNotStockAvailableException.class)
    @ResponseStatus(CONFLICT)
    public ErrorResponse handleException(BookNotStockAvailableException e) {
        return new ErrorResponse("Book.Not.Stock.Available", e.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ErrorResponse handleException(UserNotFoundException e) {
        return new ErrorResponse("User.Not.Found", e.getMessage());
    }

    @ExceptionHandler(UserNotActiveException.class)
    @ResponseStatus(FORBIDDEN)
    public ErrorResponse handleException(UserNotActiveException e) {
        return new ErrorResponse("User.Not.Active", e.getMessage());
    }

    @ExceptionHandler(UserReservationLimitExceededException.class)
    @ResponseStatus(CONFLICT)
    public ErrorResponse handleException(UserReservationLimitExceededException e) {
        return new ErrorResponse("User.Reservation.Limit", e.getMessage());
    }

}
