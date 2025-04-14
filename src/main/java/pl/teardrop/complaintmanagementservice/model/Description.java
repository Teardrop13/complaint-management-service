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
public class Description {

    public static final int MAX_LENGTH = 1000;

    private String text;

    public Description(String text) {
        if (StringUtils.isBlank(text)) {
            throw new InvalidArgumentException("Description can't be blank.");
        }
        if (text.length() > MAX_LENGTH) {
            throw new InvalidArgumentException("Description with length " + text.length() + " exceeds max length.");
        }
        this.text = text;
    }
}
