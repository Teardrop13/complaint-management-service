package pl.teardrop.complaintmanagementservice.dto;

import java.time.LocalDateTime;

public record ComplaintDto(
        Long id,
        Long productId,
        String description,
        LocalDateTime creationDate,
        Long userId,
        String country,
        Integer submissionCounter
) {

}
