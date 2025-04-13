package pl.teardrop.complaintmanagementservice.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import pl.teardrop.complaintmanagementservice.dto.Ip;
import pl.teardrop.complaintmanagementservice.model.Country;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RestClientTest(CountryProvider.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CountryProviderTest {

    @Value("${complaints.country.api.url}")
    private String countryApiUrl;

    @Autowired
    private CountryProvider countryProvider;

    @Autowired
    private MockRestServiceServer mockServer;

    @Test
    void getCountry_shouldReturnCountry_whenApiRespondsSuccessfully() {
        // given
        Ip ip = new Ip("123.123.123.123");
        mockServer.expect(requestTo(countryApiUrl + "/123.123.123.123"))
                .andRespond(withSuccess(
                        """
                                {
                                  "ip": "123.123.123.123",
                                  "country": "PL"
                                }
                                """,
                        MediaType.APPLICATION_JSON));

        // when
        Country result = countryProvider.getCountry(ip);

        // then
        assertThat(result.getCode()).isEqualTo("PL");
    }

    @Test
    void getCountry_shouldReturnUnknown_whenResponseBodyIsBlank() {
        // given
        Ip ip = new Ip("123.123.123.123");
        mockServer.expect(requestTo(countryApiUrl + "/123.123.123.123"))
                .andRespond(withSuccess());

        // when
        Country result = countryProvider.getCountry(ip);

        // then
        assertThat(result).isEqualTo(Country.UNKNOWN);
    }

    @Test
    void getCountry_shouldReturnUnknown_whenApiRespondsWithError() {
        // given
        Ip ip = new Ip("123.123.123.123");
        mockServer.expect(requestTo(countryApiUrl + "/123.123.123.123"))
                .andRespond(withStatus(HttpStatus.BAD_REQUEST));

        // when
        Country result = countryProvider.getCountry(ip);

        //then
        assertThat(result).isEqualTo(Country.UNKNOWN);
    }
}