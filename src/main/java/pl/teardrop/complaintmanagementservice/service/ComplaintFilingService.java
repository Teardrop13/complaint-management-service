package pl.teardrop.complaintmanagementservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.teardrop.complaintmanagementservice.dto.ComplaintDto;
import pl.teardrop.complaintmanagementservice.dto.FileComplaintCommand;
import pl.teardrop.complaintmanagementservice.dto.Ip;
import pl.teardrop.complaintmanagementservice.model.Complaint;
import pl.teardrop.complaintmanagementservice.model.ProductId;
import pl.teardrop.complaintmanagementservice.model.UserId;
import pl.teardrop.complaintmanagementservice.repository.ComplaintRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ComplaintFilingService {

    private final ComplaintRepository complaintRepository;
    private final ComplaintFactory complaintFactory;

    public ComplaintDto file(FileComplaintCommand command, String xForwarderForHeader) {
        Optional<Complaint> complaintOpt = complaintRepository.findByUserIdAndProductId(new UserId(command.userId()), new ProductId(command.productId()));

        Complaint complaint;
        if (complaintOpt.isPresent()) {
            log.info("Complaint for user id={} for product id={} exists, increasing submission counter", command.userId(), command.productId());
            complaint = complaintOpt.get();
            complaint.increaseSubmissionCounter();
            complaintRepository.save(complaint);
            log.info("Updated submission counter for complaint with id={} for user={}", complaint.getId(), command.userId());
        } else {
            log.info("Creating complaint for user id={} for product id={}", command.userId(), command.productId());
            Ip userIp = IpExtractor.extractUsersIp(xForwarderForHeader);
            log.debug("From X-FORWARDED-FOR header: \"{}\" extracted user's ip: {}", xForwarderForHeader, userIp.value());
            complaint = complaintFactory.get(command, userIp);
            complaintRepository.save(complaint);
            log.info("Saved complaint with id={} for user={}", complaint.getId(), command.userId());
        }
        return complaint.toDto();
    }
}
