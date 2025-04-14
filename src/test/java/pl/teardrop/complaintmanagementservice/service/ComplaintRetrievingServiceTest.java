package pl.teardrop.complaintmanagementservice.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import pl.teardrop.complaintmanagementservice.dto.ComplaintDto;
import pl.teardrop.complaintmanagementservice.dto.ComplaintListDto;
import pl.teardrop.complaintmanagementservice.exceptions.NotFoundException;
import pl.teardrop.complaintmanagementservice.model.Complaint;
import pl.teardrop.complaintmanagementservice.model.Country;
import pl.teardrop.complaintmanagementservice.model.Description;
import pl.teardrop.complaintmanagementservice.model.ProductId;
import pl.teardrop.complaintmanagementservice.model.UserId;
import pl.teardrop.complaintmanagementservice.repository.ComplaintRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ComplaintRetrievingServiceTest {

    @Mock
    private ComplaintRepository complaintRepository;

    @InjectMocks
    private ComplaintRetrievingService complaintRetrievingService;

    @Test
    void getById_shouldReturnComplaint() {
        // given
        long id = 1L;
        Complaint complaint = new Complaint(new ProductId(1L), new Description("description"), LocalDateTime.now(), new UserId(2L), new Country("PL"));
        when(complaintRepository.findById(id)).thenReturn(Optional.of(complaint));

        // when
        ComplaintDto result = complaintRetrievingService.getById(id);

        // then
        assertEquals("description", result.description());
    }

    @Test
    void getById_shouldThrow_whenComplaintNotFound() {
        // given
        long id = 1L;
        when(complaintRepository.findById(id)).thenReturn(Optional.empty());

        // when
        NotFoundException ex = assertThrows(NotFoundException.class, () -> {
            complaintRetrievingService.getById(id);
        });

        // then
        assertTrue(ex.getMessage().contains("Complaint with id " + id + " not found"));
    }

    @Test
    void getByUserId_shouldReturnPaginatedComplaintsForUser() {
        // given
        long userId = 1L;
        int page = 0;
        int size = 3;

        ArrayList<Complaint> complaints = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            complaints.add(new Complaint(new ProductId(1L),
                                         new Description("description"),
                                         LocalDateTime.now(),
                                         new UserId(2L),
                                         new Country("PL")));
        }

        when(complaintRepository.countByUserId(new UserId(userId)))
                .thenReturn(size);
        when(complaintRepository.findAllByUserId(new UserId(userId), PageRequest.of(page, size)))
                .thenReturn(complaints);

        // when
        ComplaintListDto result = complaintRetrievingService.getByUserId(userId, page, size);

        // then
        assertEquals(size, result.count());
        assertEquals(page, result.page());
        assertEquals(size, result.pageSize());
        assertEquals(size, result.complaints().size());
    }
}