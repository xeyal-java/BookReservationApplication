package com.example.bookreservationapplication.dto;

import lombok.Data;

@Data
public class ReservationRequest {
    private Long userId;
    private Long bookId;
}