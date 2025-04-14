package pl.teardrop.complaintmanagementservice.service;

import org.junit.jupiter.api.Test;
import pl.teardrop.complaintmanagementservice.dto.Ip;
import pl.teardrop.complaintmanagementservice.exceptions.InvalidArgumentException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class IpExtractorTest {

    @Test
    void extractUsersIp_shouldReturnFirstIpFromHeader() {
        String header = "192.168.1.1,10.0.0.1,127.0.0.1";
        Ip ip = IpExtractor.extractUsersIp(header);
        assertEquals("192.168.1.1", ip.value());
    }

    @Test
    void extractUsersIp_shouldTrimWhiteSpaces() {
        String header = " 192.168.1.1, 10.0.0.1";
        Ip ip = IpExtractor.extractUsersIp(header);
        assertEquals("192.168.1.1", ip.value());
    }

    @Test
    void extractUsersIp_shouldThrowWhenHeaderIsEmpty() {
        assertThrows(InvalidArgumentException.class, () -> IpExtractor.extractUsersIp(""));
    }

}