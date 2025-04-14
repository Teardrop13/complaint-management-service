package pl.teardrop.complaintmanagementservice.model;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import pl.teardrop.complaintmanagementservice.exceptions.InvalidArgumentException;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class Country {

    public static final Country UNKNOWN = new Country("UNKNOWN");
    private String code;

    public Country(String code) {
        if (StringUtils.isBlank(code)) {
            throw new InvalidArgumentException("Country can't be blank.");
        }
        this.code = code;
    }
}
