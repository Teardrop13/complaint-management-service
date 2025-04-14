package pl.teardrop.complaintmanagementservice.service;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.teardrop.complaintmanagementservice.dto.Ip;
import pl.teardrop.complaintmanagementservice.exceptions.InvalidArgumentException;

import java.util.Arrays;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IpExtractor {

    public static Ip extractUsersIp(String xForwardedForHeader) {
        return Arrays.stream(xForwardedForHeader.split(","))
                .map(String::trim)
                .findFirst()
                .map(Ip::new)
                .orElseThrow(() -> new InvalidArgumentException("User's ip not found in X-Forwarder-For header: " + xForwardedForHeader));
    }
}
