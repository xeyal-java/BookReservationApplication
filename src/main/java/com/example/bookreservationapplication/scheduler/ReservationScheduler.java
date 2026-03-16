package com.example.bookreservationapplication.scheduler;
import com.example.bookreservationapplication.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;


@RequiredArgsConstructor
public class ReservationScheduler {

    private final BookService bookService;

    @Scheduled(fixedRate = 600_000)
    public void cleanupExpiredReservations() {
        bookService.deleteExpiredReservations();
    }
}
