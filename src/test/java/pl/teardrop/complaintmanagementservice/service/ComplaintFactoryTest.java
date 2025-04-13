package pl.teardrop.complaintmanagementservice.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.teardrop.complaintmanagementservice.dto.FileComplaintCommand;
import pl.teardrop.complaintmanagementservice.dto.Ip;
import pl.teardrop.complaintmanagementservice.model.Complaint;
import pl.teardrop.complaintmanagementservice.model.Country;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ComplaintFactoryTest {

    @Mock
    private CountryProvider countryProvider;

    @InjectMocks
    private ComplaintFactory complaintFactory;

    @Test
    void get_shouldFillComplaintFieldsCorrectly() {
        // given
        FileComplaintCommand command = new FileComplaintCommand(1L,
                                                                "description",
                                                                2L);
        Ip ip = new Ip("123.123.123.123");
        Country country = new Country("PL");

        when(countryProvider.getCountry(ip)).thenReturn(country);

        // when
        Complaint complaint = complaintFactory.get(command, ip);

        // then
        assertNotNull(complaint);
        assertEquals(1L, complaint.getProductId().getId());
        assertEquals("description", complaint.getDescription().getText());
        assertEquals(2L, complaint.getUserId().getId());
        assertEquals(country, complaint.getCountry());
        assertNotNull(complaint.getCreationDate());
    }
}