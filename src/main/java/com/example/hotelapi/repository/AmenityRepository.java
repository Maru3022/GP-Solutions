package com.example.hotelapi.repository;

import com.example.hotelapi.entity.Amenity;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AmenityRepository extends JpaRepository<Amenity, Long> {

    Optional<Amenity> findByNameIgnoreCase(String name);

    @Query("select a from Amenity a where lower(a.name) in :normalizedNames")
    List<Amenity> findAllByNormalizedNames(Collection<String> normalizedNames);

    @Query("select a.name, count(h) from Hotel h join h.amenities a group by a.name")
    List<Object[]> countHotelsByAmenity();
}
