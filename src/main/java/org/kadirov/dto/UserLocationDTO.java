package org.kadirov.dto;

public record UserLocationDTO(
        String paramLocationName,
        int userId,
        double latitude,
        double longitude
) {
}
