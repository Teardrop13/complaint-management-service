package pl.teardrop.complaintmanagementservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record FileComplaintCommand(
        @NotNull Long productId,
        @NotBlank String description,
        @NotNull Long userId
) {

}
