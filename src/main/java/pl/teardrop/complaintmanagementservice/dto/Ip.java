package pl.teardrop.complaintmanagementservice.dto;

import org.apache.commons.lang3.StringUtils;

public record Ip(String value) {

    public Ip {
        if (StringUtils.isBlank(value)) {
            throw new IllegalArgumentException("Ip can't be blank.");
        }
    }
}
