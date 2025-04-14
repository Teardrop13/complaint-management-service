package pl.teardrop.complaintmanagementservice.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.teardrop.complaintmanagementservice.dto.ComplaintDto;
import pl.teardrop.complaintmanagementservice.dto.UpdateComplaintCommand;
import pl.teardrop.complaintmanagementservice.exceptions.NotFoundException;
import pl.teardrop.complaintmanagementservice.model.Complaint;
import pl.teardrop.complaintmanagementservice.model.Country;
import pl.teardrop.complaintmanagementservice.model.Description;
import pl.teardrop.complaintmanagementservice.model.ProductId;
import pl.teardrop.complaintmanagementservice.model.UserId;
import pl.teardrop.complaintmanagementservice.repository.ComplaintRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ComplaintUpdatingServiceTest {

    @Mock
    private ComplaintRepository complaintRepository;

    @InjectMocks
    private ComplaintUpdatingService complaintUpdatingService;

    @Test
    void update_shouldChangeOnlyDescription() {
        // given
        long id = 1L;
        UpdateComplaintCommand command = new UpdateComplaintCommand("Updated description");

        Complaint existingComplaint = new Complaint(new ProductId(1L), new Description("Original description"), LocalDateTime.now(), new UserId(2L), new Country("PL"));
        ComplaintDto existingComplaintDto = existingComplaint.toDto();
        when(complaintRepository.findById(id)).thenReturn(Optional.of(existingComplaint));

        // when
        ComplaintDto result = complaintUpdatingService.update(id, command);

        // then
        assertEquals("Updated description", result.description());
        assertEquals(existingComplaintDto.productId(), result.productId());
        assertEquals(existingComplaintDto.creationDate().truncatedTo(ChronoUnit.MILLIS), result.creationDate().truncatedTo(ChronoUnit.MILLIS));
        assertEquals(existingComplaintDto.userId(), result.userId());
        assertEquals(existingComplaintDto.country(), result.country());
        assertEquals(existingComplaintDto.submissionCounter(), result.submissionCounter());
        Mockito.verify(complaintRepository).save(existingComplaint);
    }

    @Test
    void update_shouldThrowException_whenComplaintNotFound() {
        // given
        long id = 1L;
        UpdateComplaintCommand command = new UpdateComplaintCommand("update");
        when(complaintRepository.findById(id)).thenReturn(Optional.empty());

        // when
        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            complaintUpdatingService.update(id, command);
        });

        // then
        assertTrue(exception.getMessage().contains("Complaint with id " + id + " not found"));
        verify(complaintRepository, never()).save(any());
    }
}