package pl.teardrop.complaintmanagementservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.teardrop.complaintmanagementservice.dto.ComplaintDto;
import pl.teardrop.complaintmanagementservice.dto.UpdateComplaintCommand;
import pl.teardrop.complaintmanagementservice.exceptions.NotFoundException;
import pl.teardrop.complaintmanagementservice.model.Complaint;
import pl.teardrop.complaintmanagementservice.model.Description;
import pl.teardrop.complaintmanagementservice.repository.ComplaintRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class ComplaintUpdatingService {

    private final ComplaintRepository complaintRepository;

    public ComplaintDto update(long id, UpdateComplaintCommand command) {
        Complaint complaint = complaintRepository.findById(id).orElseThrow(() -> new NotFoundException("Complaint with id " + id + " not found"));
        complaint.setDescription(new Description(command.description()));
        complaintRepository.save(complaint);
        log.info("Updated description for complaint with id={} for user={}", complaint.getId(), complaint.getUserId().getId());
        return complaint.toDto();
    }
}
