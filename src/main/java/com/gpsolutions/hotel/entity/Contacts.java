package com.gpsolutions.hotel.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entity representing hotel contact information.
 * Has a one-to-one relationship with Hotel.
 */
@Entity
@Table(name = "contacts")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString(of = {"id", "phone", "email"})
public class Contacts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String email;

    @OneToOne(mappedBy = "contacts")
    private Hotel hotel;
}
