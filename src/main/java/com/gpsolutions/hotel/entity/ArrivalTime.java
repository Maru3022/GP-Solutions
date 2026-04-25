package com.gpsolutions.hotel.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Embedded entity for hotel arrival and departure times.
 * Format: "HH:mm" (e.g., "15:00", "11:00")
 */
@Embeddable
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArrivalTime {

    private String checkIn;
    private String checkOut;
}
