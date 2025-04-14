package pl.teardrop.complaintmanagementservice.controller;

import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import pl.teardrop.complaintmanagementservice.dto.ComplaintDto;
import pl.teardrop.complaintmanagementservice.dto.FileComplaintCommand;
import pl.teardrop.complaintmanagementservice.dto.UpdateComplaintCommand;
import pl.teardrop.complaintmanagementservice.model.Country;
import pl.teardrop.complaintmanagementservice.repository.ComplaintRepository;
import pl.teardrop.complaintmanagementservice.service.ComplaintFilingService;
import pl.teardrop.complaintmanagementservice.service.CountryProvider;

import java.time.temporal.ChronoUnit;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ComplaintControllerIT {

    @LocalServerPort
    public int serverPort;

    @MockitoBean
    private CountryProvider countryProvider;

    @Autowired
    private ComplaintRepository complaintRepository;

    @Autowired
    private ComplaintFilingService complaintFilingService;

    @PostConstruct
    public void setup() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        RestAssured.port = serverPort;

        when(countryProvider.getCountry(any())).thenReturn(new Country("PL"));
    }

    @BeforeEach
    void setUp() {
        complaintRepository.deleteAll();
    }

    // Creating

    @Test
    void fileComplaint_shouldCreateComplaint_whenFilingComplaintFirstTime() {
        FileComplaintCommand command = new FileComplaintCommand(1L, "description", 2L);

        ComplaintDto createdComplaint = given()
                .header("X-FORWARDED-FOR", "123.123.123.123")
                .contentType(ContentType.JSON)
                .body(command)
                .post("/complaints")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract().as(ComplaintDto.class);

        assertNotNull(createdComplaint.id());
        assertEquals(1L, createdComplaint.productId());
        assertEquals("description", createdComplaint.description());
        assertNotNull(createdComplaint.creationDate());
        assertEquals(2L, createdComplaint.userId());
        assertEquals("PL", createdComplaint.country());
        assertEquals(1, createdComplaint.submissionCounter());
    }

    @Test
    void fileComplaint_shouldUpdateComplaint_whenFilingComplaintDuplicate() {
        FileComplaintCommand command = new FileComplaintCommand(1L, "description", 2L);
        FileComplaintCommand commandDuplicate = new FileComplaintCommand(
                command.productId(),
                "changed description",
                command.userId()
        );

        complaintFilingService.file(command, "123.123.123.123");

        ComplaintDto updatedComplaint = given()
                .header("X-FORWARDED-FOR", "123.123.123.123")
                .contentType(ContentType.JSON)
                .body(commandDuplicate)
                .post("/complaints")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract().as(ComplaintDto.class);

        assertNotNull(updatedComplaint.id());
        assertEquals(1L, updatedComplaint.productId());
        assertEquals("description", updatedComplaint.description());
        assertNotNull(updatedComplaint.creationDate());
        assertEquals(2L, updatedComplaint.userId());
        assertEquals("PL", updatedComplaint.country());
        assertEquals(2, updatedComplaint.submissionCounter());
    }

    @Test
    void fileComplaint_shouldReturnBadRequest_whenIncorrectRequestProvided() {
        FileComplaintCommand command = new FileComplaintCommand(2L, null, 3L);

        given()
                .header("X-FORWARDED-FOR", "123.123.123.123")
                .contentType(ContentType.JSON)
                .body(command)
                .post("/complaints")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    // Updating

    @Test
    void updateComplaint_shouldUpdateOnlyDescription() {
        FileComplaintCommand command = new FileComplaintCommand(1L, "description", 2L);
        UpdateComplaintCommand updateCommand = new UpdateComplaintCommand("changed description");

        ComplaintDto existingComplaint = complaintFilingService.file(command, "123.123.123.123");

        ComplaintDto updatedComplaint = given()
                .contentType(ContentType.JSON)
                .body(updateCommand)
                .put("/complaints/" + existingComplaint.id())
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract().as(ComplaintDto.class);

        assertEquals(existingComplaint.id(), updatedComplaint.id());
        assertEquals(existingComplaint.productId(), updatedComplaint.productId());
        assertEquals(updateCommand.description(), updatedComplaint.description());
        assertEquals(existingComplaint.creationDate().truncatedTo(ChronoUnit.MILLIS), updatedComplaint.creationDate().truncatedTo(ChronoUnit.MILLIS));
        assertEquals(existingComplaint.userId(), updatedComplaint.userId());
        assertEquals(existingComplaint.country(), updatedComplaint.country());
        assertEquals(existingComplaint.submissionCounter(), updatedComplaint.submissionCounter());
    }

    @Test
    void updateComplaint_shouldReturnNotFound_whenComplaintDoesntExist() {
        UpdateComplaintCommand updateCommand = new UpdateComplaintCommand("changed description");

        given()
                .contentType(ContentType.JSON)
                .body(updateCommand)
                .put("/complaints/123")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }
}