package com.example.bookreservationapplication.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SchedulerService {

    private final BookService bookService;

    @Scheduled(fixedRate = 600000)
    public void runCleanup() {
        bookService.deleteExpiredReservations();
    }
}