package com.example.bookreservationapplication.exception;

public class BookNotStockAvailableException extends RuntimeException {
    public BookNotStockAvailableException(String message) {
        super(message);
    }
}
