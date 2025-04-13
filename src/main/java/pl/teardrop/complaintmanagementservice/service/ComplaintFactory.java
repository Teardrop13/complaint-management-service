package pl.teardrop.complaintmanagementservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.teardrop.complaintmanagementservice.dto.FileComplaintCommand;
import pl.teardrop.complaintmanagementservice.dto.Ip;
import pl.teardrop.complaintmanagementservice.model.Complaint;
import pl.teardrop.complaintmanagementservice.model.Description;
import pl.teardrop.complaintmanagementservice.model.ProductId;
import pl.teardrop.complaintmanagementservice.model.UserId;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ComplaintFactory {

    private final CountryProvider countryProvider;

    public Complaint get(FileComplaintCommand command, Ip ip) {
        return new Complaint(
                new ProductId(command.productId()),
                new Description(command.description()),
                LocalDateTime.now(),
                new UserId(command.userId()),
                countryProvider.getCountry(ip)
        );
    }
}
