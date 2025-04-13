package pl.teardrop.complaintmanagementservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;
import pl.teardrop.complaintmanagementservice.dto.GeolocationApiResponse;
import pl.teardrop.complaintmanagementservice.dto.Ip;
import pl.teardrop.complaintmanagementservice.model.Country;

import java.net.URI;

@Service
@Slf4j
public class CountryProvider {

    private final RestClient restClient;

    public CountryProvider(@Value("${complaints.country.api.url}") String countryApiUrl,
                           RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.baseUrl(countryApiUrl).build();
    }

    public Country getCountry(Ip ip) {
        URI uri = UriComponentsBuilder.fromPath("/{ip}").build(ip.value());

        try {
            GeolocationApiResponse response = restClient.get()
                    .uri(uri)
                    .retrieve()
                    .body(GeolocationApiResponse.class);

            if (response != null && StringUtils.isNotBlank(response.country())) {
                Country country = new Country(response.country());
                log.info("Country recognised for ip={} -> {}", ip.value(), country.getCode());
                return country;
            } else {
                log.error("Received empty response from geolocation api");
                return Country.UNKNOWN;
            }
        } catch (HttpClientErrorException e) {
            log.error("Country was not recognised for ip={}", ip.value(), e);
            return Country.UNKNOWN;
        }
    }
}
