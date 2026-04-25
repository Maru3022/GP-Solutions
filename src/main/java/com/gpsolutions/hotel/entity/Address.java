package com.gpsolutions.hotel.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entity representing hotel address information.
 * Has a one-to-one relationship with Hotel.
 */
@Entity
@Table(name = "address")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString(of = {"id", "city", "country"})
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "house_number", nullable = false)
    private String houseNumber;

    @Column(nullable = false)
    private String street;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String country;

    @Column(name = "post_code", nullable = false)
    private String postCode;

    @OneToOne(mappedBy = "address")
    private Hotel hotel;
}
