package pl.teardrop.complaintmanagementservice.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.teardrop.complaintmanagementservice.dto.ComplaintDto;
import pl.teardrop.complaintmanagementservice.dto.FileComplaintCommand;
import pl.teardrop.complaintmanagementservice.dto.Ip;
import pl.teardrop.complaintmanagementservice.model.Complaint;
import pl.teardrop.complaintmanagementservice.model.Country;
import pl.teardrop.complaintmanagementservice.model.Description;
import pl.teardrop.complaintmanagementservice.model.ProductId;
import pl.teardrop.complaintmanagementservice.model.UserId;
import pl.teardrop.complaintmanagementservice.repository.ComplaintRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ComplaintFilingServiceTest {

    @Mock
    private ComplaintFactory complaintFactory;

    @Mock
    private ComplaintRepository complaintRepository;

    @InjectMocks
    private ComplaintFilingService complaintFilingService;

    @Test
    void file_shouldUpdateSubmissionCounterWithoutChangingOtherFields_whenAddingDuplicate() {
        // given
        ProductId productId = new ProductId(1L);
        Description description = new Description("description");
        LocalDateTime creationDate = LocalDateTime.now();
        UserId userId = new UserId(1L);
        Country country = new Country("PL");

        Complaint complaint = new Complaint(productId, description, creationDate, userId, country);
        FileComplaintCommand command = new FileComplaintCommand(productId.getId(), "new description", userId.getId());

        when(complaintRepository.findByUserIdAndProductId(any(), any()))
                .thenReturn(Optional.of(complaint));

        // when
        ComplaintDto updatedComplaint = complaintFilingService.file(command, "123.123.123.123");

        // then
        assertEquals(productId.getId(), updatedComplaint.productId());
        assertEquals(description.getText(), updatedComplaint.description());
        assertEquals(creationDate, updatedComplaint.creationDate());
        assertEquals(userId.getId(), updatedComplaint.userId());
        assertEquals(country.getCode(), updatedComplaint.country());
        assertEquals(2, updatedComplaint.submissionCounter());

        verify(complaintRepository).save(complaint);
        verify(complaintFactory, never()).get(any(), any());
    }

    @Test
    void file_shouldCreateNewComplaint_whenAddingNotDuplicate() {
        // given
        FileComplaintCommand command = new FileComplaintCommand(1L, "description", 2L);
        String xForwardedForHeader = "123.123.123.123";
        Ip extractedIp = new Ip(xForwardedForHeader);
        Complaint complaint = new Complaint(new ProductId(1L), new Description("description"), LocalDateTime.now(), new UserId(2L), new Country("PL"));

        when(complaintRepository.findByUserIdAndProductId(any(), any()))
                .thenReturn(Optional.empty());
        when(complaintFactory.get(command, extractedIp)).thenReturn(complaint);

        // when
        complaintFilingService.file(command, xForwardedForHeader);

        // then
        verify(complaintFactory).get(command, extractedIp);
        verify(complaintRepository).save(complaint);
    }
}