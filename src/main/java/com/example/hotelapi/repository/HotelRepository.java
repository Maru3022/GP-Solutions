package com.example.hotelapi.repository;

import com.example.hotelapi.entity.Hotel;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface HotelRepository extends JpaRepository<Hotel, Long>, JpaSpecificationExecutor<Hotel> {

    @Override
    @EntityGraph(attributePaths = "amenities")
    List<Hotel> findAll();

    @Query("select distinct h from Hotel h left join fetch h.amenities where h.id = :id")
    Optional<Hotel> findWithAmenitiesById(@Param("id") Long id);

    @Query("select h.brand, count(h) from Hotel h group by h.brand")
    List<Object[]> countByBrand();

    @Query("select h.city, count(h) from Hotel h group by h.city")
    List<Object[]> countByCity();

    @Query("select h.country, count(h) from Hotel h group by h.country")
    List<Object[]> countByCountry();
}
