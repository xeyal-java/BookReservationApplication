package com.example.bookreservationapplication.service;
import com.example.bookreservationapplication.dao.Entity.ReservationEntity;
import com.example.bookreservationapplication.dao.repository.ReservationRepository;
import com.example.bookreservationapplication.enums.ReservationStatus;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Data
@RequiredArgsConstructor
public class SchedulerService {

    private final ReservationRepository reservationRepository;


    @Scheduled(fixedRate = 60000)
    public void deleteExpiredReservations() {
        LocalDateTime twoHoursAgo = LocalDateTime.now().minusHours(2);

        List<ReservationEntity> expiredReservations = reservationRepository.findByStatusAndCreatedAtBefore(
                ReservationStatus.PENDING,
                twoHoursAgo
        );
        reservationRepository.deleteAll(expiredReservations);
    }
}
