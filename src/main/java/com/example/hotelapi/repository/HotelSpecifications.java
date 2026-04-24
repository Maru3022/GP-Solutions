package com.example.hotelapi.repository;

import com.example.hotelapi.entity.Amenity;
import com.example.hotelapi.entity.Hotel;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import java.util.Collection;
import java.util.Locale;
import org.springframework.data.jpa.domain.Specification;

public final class HotelSpecifications {

    private HotelSpecifications() {
    }

    public static Specification<Hotel> nameContains(String value) {
        return (root, query, criteriaBuilder) -> value == null || value.isBlank()
                ? null
                : criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("name")),
                        wrapLike(value));
    }

    public static Specification<Hotel> brandEquals(String value) {
        return (root, query, criteriaBuilder) -> value == null || value.isBlank()
                ? null
                : criteriaBuilder.equal(criteriaBuilder.lower(root.get("brand")), normalize(value));
    }

    public static Specification<Hotel> cityEquals(String value) {
        return (root, query, criteriaBuilder) -> value == null || value.isBlank()
                ? null
                : criteriaBuilder.equal(criteriaBuilder.lower(root.get("city")), normalize(value));
    }

    public static Specification<Hotel> countryEquals(String value) {
        return (root, query, criteriaBuilder) -> value == null || value.isBlank()
                ? null
                : criteriaBuilder.equal(criteriaBuilder.lower(root.get("country")), normalize(value));
    }

    public static Specification<Hotel> hasAmenities(Collection<String> amenities) {
        return (root, query, criteriaBuilder) -> {
            if (amenities == null || amenities.isEmpty()) {
                return null;
            }
            query.distinct(true);
            Join<Hotel, Amenity> amenityJoin = root.join("amenities", JoinType.LEFT);
            return criteriaBuilder.lower(amenityJoin.get("name"))
                    .in(amenities.stream().map(HotelSpecifications::normalize).toList());
        };
    }

    private static String wrapLike(String value) {
        return "%" + normalize(value) + "%";
    }

    private static String normalize(String value) {
        return value.trim().toLowerCase(Locale.ROOT);
    }
}
