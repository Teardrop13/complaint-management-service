package pl.teardrop.complaintmanagementservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.teardrop.complaintmanagementservice.dto.ComplaintDto;
import pl.teardrop.complaintmanagementservice.dto.FileComplaintCommand;
import pl.teardrop.complaintmanagementservice.dto.UpdateComplaintCommand;
import pl.teardrop.complaintmanagementservice.service.ComplaintFilingService;
import pl.teardrop.complaintmanagementservice.service.ComplaintUpdatingService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/complaints")
@Slf4j
public class ComplaintController {

    private static final String X_FORWARDED_FOR = "X-FORWARDED-FOR";

    private final ComplaintFilingService complaintFilingService;
    private final ComplaintUpdatingService complaintUpdatingService;

    @PostMapping
    public ResponseEntity<ComplaintDto> fileComplaint(@RequestBody @Valid FileComplaintCommand command,
                                                      @RequestHeader(X_FORWARDED_FOR) String xForwarderFor) {
        log.info("Received POST /complaints with body: {}", command);
        ComplaintDto complaint = complaintFilingService.file(command, xForwarderFor);
        if (complaint.submissionCounter() == 1) {
            return ResponseEntity
                    .created(ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(complaint.id()).toUri()) // todo get endpoint
                    .body(complaint);
        } else {
            return ResponseEntity.ok(complaint);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ComplaintDto> updateComplaint(@PathVariable long id,
                                                        @RequestBody @Valid UpdateComplaintCommand command) {
        log.info("Received PUT /complaints/{} with body: {}", id, command);
        ComplaintDto complaint = complaintUpdatingService.update(id, command);
        return ResponseEntity.ok(complaint);
    }
}
