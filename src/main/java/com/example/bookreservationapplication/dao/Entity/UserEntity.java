package com.example.bookreservationapplication.dao.Entity;

import com.example.bookreservationapplication.enums.role;
import jakarta.persistence.*;
import lombok.*;



@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private boolean active;

    @Enumerated(EnumType.STRING)
    private role role;
}