package com.gpsolutions.hotel.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Entity representing hotel amenities.
 * Has a many-to-many relationship with Hotel through hotel_amenities table.
 */
@Entity
@Table(name = "amenity")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString(of = {"id", "name"})
public class Amenity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @ManyToMany(mappedBy = "amenities", fetch = FetchType.LAZY)
    @Builder.Default
    private Set<Hotel> hotels = new HashSet<>();
}
