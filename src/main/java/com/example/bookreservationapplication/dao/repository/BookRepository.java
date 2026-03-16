package com.example.bookreservationapplication.dao.repository;

import com.example.bookreservationapplication.dao.Entity.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, Long> {
}
