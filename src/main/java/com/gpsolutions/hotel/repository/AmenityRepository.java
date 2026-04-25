package com.gpsolutions.hotel.repository;

import com.gpsolutions.hotel.entity.Amenity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for Amenity entity.
 * Provides CRUD operations for amenities.
 */
@Repository
public interface AmenityRepository extends JpaRepository<Amenity, Long> {

    /**
     * Find amenity by name.
     */
    Optional<Amenity> findByName(String name);
}
