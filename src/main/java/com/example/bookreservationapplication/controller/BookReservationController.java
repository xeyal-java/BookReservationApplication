package com.example.bookreservationapplication.controller;

import com.example.bookreservationapplication.dao.Entity.BookEntity;
import com.example.bookreservationapplication.dao.Entity.ReservationEntity;
import com.example.bookreservationapplication.dao.Entity.UserEntity;
import com.example.bookreservationapplication.dto.ReservationRequest;
import com.example.bookreservationapplication.dto.ResponseDto;
import com.example.bookreservationapplication.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BookReservationController {

    private final BookService bookService;

    @PostMapping("/reservations")
    public ResponseEntity<ResponseDto> createReservation(@RequestBody ReservationRequest request) {
        bookService.createReservation(request.getUserId(), request.getBookId());
        return ResponseEntity.ok(new ResponseDto("Reservation successfully created. Waiting for admin approval."));
    }

    @PostMapping("/reservations/{resId}/approve")
    public ResponseEntity<ResponseDto> approveReservation(
            @PathVariable Long resId,
            @RequestHeader Long adminId) {
        bookService.approveReservation(resId, adminId);
        return ResponseEntity.ok(new ResponseDto("Reservation has been approved by admin."));
    }

    @GetMapping("/users/{userId}/reservations")
    public List<ReservationEntity> getUserReservations(@PathVariable Long userId) {
        return bookService.getReservationsByUser(userId);
    }

    @GetMapping("/users")
    public List<UserEntity> getAllUsers() {
        return bookService.getAllUsers();
    }

    @GetMapping("/books")
    public List<BookEntity> getAllBooks() {
        return bookService.getAllBooks();
    }
}