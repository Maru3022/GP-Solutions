package com.gpsolutions.hotel.repository;

import com.gpsolutions.hotel.entity.Hotel;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * JPA Specification for dynamic Hotel filtering.
 * Used for /property-view/search endpoint.
 */
@NoArgsConstructor
@AllArgsConstructor
public class HotelSpecification implements Specification<Hotel> {

    private static final long serialVersionUID = 1L;

    private String name;
    private String brand;
    private String city;
    private String country;
    private List<String> amenities;

    @Override
    public Predicate toPredicate(Root<Hotel> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();

        // Filter by hotel name (case-insensitive, contains)
        if (name != null && !name.isEmpty()) {
            predicates.add(cb.like(
                    cb.lower(root.get("name")),
                    "%" + name.toLowerCase(Locale.ROOT) + "%"
            ));
        }

        // Filter by brand (case-insensitive, contains)
        if (brand != null && !brand.isEmpty()) {
            predicates.add(cb.like(
                    cb.lower(root.get("brand")),
                    "%" + brand.toLowerCase(Locale.ROOT) + "%"
            ));
        }

        // Filter by city (case-insensitive, contains)
        if (city != null && !city.isEmpty()) {
            predicates.add(cb.like(
                    cb.lower(root.get("address").get("city")),
                    "%" + city.toLowerCase(Locale.ROOT) + "%"
            ));
        }

        // Filter by country (case-insensitive, contains)
        if (country != null && !country.isEmpty()) {
            predicates.add(cb.like(
                    cb.lower(root.get("address").get("country")),
                    "%" + country.toLowerCase(Locale.ROOT) + "%"
            ));
        }

        // Filter by amenities (all specified amenities must be present)
        if (amenities != null && !amenities.isEmpty()) {
            Join<?, ?> amenityJoin = root.join("amenities");
            predicates.add(amenityJoin.get("name").in(amenities));
            query.distinct(true);
        }

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
