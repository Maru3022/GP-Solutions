package com.gpsolutions.hotel.repository;

import com.gpsolutions.hotel.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for Hotel entity.
 * Provides CRUD operations and custom queries.
 */
@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long>, JpaSpecificationExecutor<Hotel> {

    /**
     * Count hotels grouped by brand.
     * Returns a Map where key is brand name and value is count.
     */
    @Query("SELECT h.brand as name, COUNT(h) as count " +
            "FROM Hotel h " +
            "GROUP BY h.brand")
    List<Object[]> countByBrand();

    /**
     * Count hotels grouped by city.
     * Returns a Map where key is city name and value is count.
     */
    @Query("SELECT a.city as name, COUNT(DISTINCT h.id) as count " +
            "FROM Hotel h " +
            "JOIN h.address a " +
            "GROUP BY a.city")
    List<Object[]> countByCity();

    /**
     * Count hotels grouped by country.
     * Returns a Map where key is country name and value is count.
     */
    @Query("SELECT a.country as name, COUNT(DISTINCT h.id) as count " +
            "FROM Hotel h " +
            "JOIN h.address a " +
            "GROUP BY a.country")
    List<Object[]> countByCountry();

    /**
     * Count hotels grouped by amenities.
     * Returns a Map where key is amenity name and value is count.
     */
    @Query("SELECT am.name as name, COUNT(DISTINCT h.id) as count " +
            "FROM Hotel h " +
            "JOIN h.amenities am " +
            "GROUP BY am.name " +
            "ORDER BY am.name")
    List<Object[]> countByAmenities();
}
