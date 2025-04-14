package pl.teardrop.complaintmanagementservice.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateComplaintCommand(
        @NotBlank String description
) {

}
