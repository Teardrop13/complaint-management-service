package pl.teardrop.complaintmanagementservice.model;

import org.junit.jupiter.api.Test;
import pl.teardrop.complaintmanagementservice.exceptions.InvalidArgumentException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CountryTest {

    @Test
    void constructor_shouldThrow_whenCountryCodeIsBlank() {
        assertThrows(InvalidArgumentException.class, () -> new Country(" "));
    }

    @Test
    void constructor_shouldThrow_whenCountryCodeIsNull() {
        assertThrows(InvalidArgumentException.class, () -> new Country(null));
    }

    @Test
    void constructor_shouldCreateCountry_whenValidCodeProvided() {
        String countryText = "PL";
        Country country = new Country(countryText);
        assertEquals(countryText, country.getCode());
    }
}