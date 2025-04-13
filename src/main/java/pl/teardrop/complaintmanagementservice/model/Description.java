package pl.teardrop.complaintmanagementservice.model;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Description {

    public static final int MAX_LENGTH = 1000;

    private String text;

    public Description(String text) {
        if (StringUtils.isBlank(text)) {
            throw new IllegalArgumentException("Description can't be blank.");
        }
        if (text.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("Description with length " + text.length() + " exceeds max length.");
        }
        this.text = text;
    }
}
