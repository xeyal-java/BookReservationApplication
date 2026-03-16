package com.example.bookreservationapplication.service;

import com.example.bookreservationapplication.dao.Entity.BookEntity;
import com.example.bookreservationapplication.dao.Entity.ReservationEntity;
import com.example.bookreservationapplication.dao.Entity.UserEntity;
import com.example.bookreservationapplication.dao.repository.BookRepository;
import com.example.bookreservationapplication.dao.repository.ReservationRepository;
import com.example.bookreservationapplication.dao.repository.UserRepository;
import com.example.bookreservationapplication.enums.ReservationStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;

    @Transactional
    public ReservationEntity createReservation(Long userId, Long bookId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!user.isActive())
            throw new RuntimeException("User is not active");

        int activeReservations = reservationRepository.countByUserIdAndStatus(userId, ReservationStatus.PENDING);
        if (activeReservations >= 3)
            throw new RuntimeException("User has reached maximum reservation limit");

        BookEntity book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        if (book.getStock() <= 0)
            throw new RuntimeException("Book is out of stock");

        ReservationEntity reservation = new ReservationEntity();
        reservation.setUser(user);
        reservation.setBook(book);
        reservation.setStatus(ReservationStatus.PENDING);
        reservation.setCreatedAt(LocalDateTime.now());

        return reservationRepository.save(reservation);
    }

    @Transactional
    public void approveReservation(Long reservationId) {
        ReservationEntity reservation = reservationRepository.findById(reservationId)
                .orElseThrow();

        reservation.setStatus(ReservationStatus.APPROVED);

        BookEntity book = reservation.getBook();
        book.setStock(book.getStock() - 1);
        bookRepository.save(book);

        reservationRepository.save(reservation);
    }

    @Transactional
    public void deleteExpiredReservations() {
        LocalDateTime twoHoursAgo = LocalDateTime.now().minusHours(2);
        reservationRepository.findByStatusAndCreatedAtBefore(ReservationStatus.PENDING, twoHoursAgo)
                .forEach(reservationRepository::delete);
    }

    public List<ReservationEntity> getReservationsByUser(Long userId) {
        return reservationRepository.findByUserId(userId);
    }

    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    public List<BookEntity> getAllBooks() {
        return bookRepository.findAll();
    }
}