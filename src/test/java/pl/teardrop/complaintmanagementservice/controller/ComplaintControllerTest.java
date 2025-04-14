package pl.teardrop.complaintmanagementservice.controller;

import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import pl.teardrop.complaintmanagementservice.dto.ComplaintDto;
import pl.teardrop.complaintmanagementservice.dto.FileComplaintCommand;
import pl.teardrop.complaintmanagementservice.service.ComplaintFilingService;

import java.time.LocalDateTime;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.endsWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ComplaintControllerTest {

    @LocalServerPort
    public int serverPort;

    @MockitoBean
    private ComplaintFilingService complaintFilingService;

    @PostConstruct
    public void setup() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        RestAssured.port = serverPort;
        RestAssured.urlEncodingEnabled = false;
    }

    @Test
    void testFileComplaint() {
        when(complaintFilingService.file(any(), any())).thenReturn(new ComplaintDto(1L, 2L, "description", LocalDateTime.now(), 3L, "PL", 1));

        FileComplaintCommand command = new FileComplaintCommand(2L, "description", 3L);

        given()
                .header("X-FORWARDED-FOR", "123.123.123.123")
                .contentType(ContentType.JSON)
                .body(command)
                .post("/complaints")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .assertThat()
                .header("Location", endsWith("/complaints/1"));
    }

    @Test
    void testFileComplaint_whenDuplicateWasProvided() {
        when(complaintFilingService.file(any(), any())).thenReturn(new ComplaintDto(1L, 2L, "description", LocalDateTime.now(), 3L, "PL", 2));

        FileComplaintCommand command = new FileComplaintCommand(2L, "description", 3L);

        given()
                .header("X-FORWARDED-FOR", "123.123.123.123")
                .contentType(ContentType.JSON)
                .body(command)
                .post("/complaints")
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void testFileComplaint_whenFieldIsMissingInCommand() {
        when(complaintFilingService.file(any(), any())).thenReturn(new ComplaintDto(1L, 2L, "description", LocalDateTime.now(), 3L, "PL", 2));

        FileComplaintCommand command = new FileComplaintCommand(2L, null, 3L);

        given()
                .header("X-FORWARDED-FOR", "123.123.123.123")
                .contentType(ContentType.JSON)
                .body(command)
                .post("/complaints")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .assertThat();
    }
}