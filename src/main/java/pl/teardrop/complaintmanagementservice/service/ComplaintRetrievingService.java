package pl.teardrop.complaintmanagementservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.teardrop.complaintmanagementservice.dto.ComplaintDto;
import pl.teardrop.complaintmanagementservice.dto.ComplaintListDto;
import pl.teardrop.complaintmanagementservice.exceptions.NotFoundException;
import pl.teardrop.complaintmanagementservice.model.Complaint;
import pl.teardrop.complaintmanagementservice.model.UserId;
import pl.teardrop.complaintmanagementservice.repository.ComplaintRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ComplaintRetrievingService {

    private final ComplaintRepository complaintRepository;

    public ComplaintDto getById(long id) {
        return complaintRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Complaint with id " + id + " not found"))
                .toDto();
    }

    public ComplaintListDto getByUserId(long userId, int page, int size) {
        UserId userIdObj = new UserId(userId);

        int count = complaintRepository.countByUserId(userIdObj);
        List<ComplaintDto> complaints = complaintRepository.findAllByUserId(userIdObj, PageRequest.of(page, size, Sort.by("id")))
                .stream()
                .map(Complaint::toDto)
                .toList();

        return new ComplaintListDto(complaints,
                                    page,
                                    size,
                                    count);
    }
}
