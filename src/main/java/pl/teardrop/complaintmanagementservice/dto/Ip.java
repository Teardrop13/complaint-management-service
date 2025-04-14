package pl.teardrop.complaintmanagementservice.dto;

import org.apache.commons.lang3.StringUtils;
import pl.teardrop.complaintmanagementservice.exceptions.InvalidArgumentException;

public record Ip(String value) {

    public Ip {
        if (StringUtils.isBlank(value)) {
            throw new InvalidArgumentException("Ip can't be blank.");
        }
    }
}
