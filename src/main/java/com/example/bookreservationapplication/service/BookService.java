package com.example.bookreservationapplication.service;

import com.example.bookreservationapplication.dao.Entity.BookEntity;
import com.example.bookreservationapplication.dao.Entity.ReservationEntity;
import com.example.bookreservationapplication.dao.Entity.UserEntity;
import com.example.bookreservationapplication.dao.repository.BookRepository;
import com.example.bookreservationapplication.dao.repository.ReservationRepository;
import com.example.bookreservationapplication.dao.repository.UserRepository;
import com.example.bookreservationapplication.enums.ReservationStatus;
import com.example.bookreservationapplication.enums.role;
import com.example.bookreservationapplication.exception.*;
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
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!user.isActive())
            throw new UserNotActiveException("User is not active");

        int pending = reservationRepository.countByUserIdAndStatus(userId, ReservationStatus.PENDING);
        int approved = reservationRepository.countByUserIdAndStatus(userId, ReservationStatus.APPROVED);

        if ((pending + approved) >= 3)
            throw new UserReservationLimitExceededException("User has reached maximum reservation limit (3 books)");

        BookEntity book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book not found"));

        if (book.getStock() <= 0)
            throw new BookNotStockAvailableException("Book is out of stock");

        ReservationEntity reservation = new ReservationEntity();
        reservation.setUser(user);
        reservation.setBook(book);
        reservation.setStatus(ReservationStatus.PENDING);
        reservation.setCreatedAt(LocalDateTime.now());

        return reservationRepository.save(reservation);
    }

    @Transactional
    public void approveReservation(Long reservationId, Long adminId) {
        UserEntity admin = userRepository.findById(adminId)
                .orElseThrow(() -> new UserNotFoundException("Admin user not found"));

        if (admin.getRole() != role.ADMIN) {
            throw new RuntimeException("Access denied: Only admins can approve reservations");
        }

        ReservationEntity reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        if (reservation.getStatus() != ReservationStatus.PENDING) {
            throw new RuntimeException("Reservation is already " + reservation.getStatus());
        }

        BookEntity book = reservation.getBook();
        if (book.getStock() <= 0) {
            throw new BookNotStockAvailableException("No stock available to fulfill this reservation");
        }

        book.setStock(book.getStock() - 1);
        bookRepository.save(book);

        reservation.setStatus(ReservationStatus.APPROVED);
        reservationRepository.save(reservation);
    }

    @Transactional
    public void deleteExpiredReservations() {
        LocalDateTime twoHoursAgo = LocalDateTime.now().minusHours(2);
        List<ReservationEntity> expired = reservationRepository
                .findByStatusAndCreatedAtBefore(ReservationStatus.PENDING, twoHoursAgo);
        reservationRepository.deleteAll(expired);
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