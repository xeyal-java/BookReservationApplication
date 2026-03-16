package com.example.bookreservationapplication.dao.repository;

import com.example.bookreservationapplication.dao.Entity.ReservationEntity;
import com.example.bookreservationapplication.enums.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {

    int countByUserIdAndStatus(Long userId, ReservationStatus status);

    List<ReservationEntity> findByStatusAndCreatedAtBefore(ReservationStatus status, LocalDateTime time);

    List<ReservationEntity> findByUserId(Long userId);

}