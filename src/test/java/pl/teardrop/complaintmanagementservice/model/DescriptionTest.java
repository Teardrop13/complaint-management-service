package pl.teardrop.complaintmanagementservice.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DescriptionTest {

    @Test
    void constructor_shouldCreateDescription_whenValidTextIsProvided() {
        String text = "Valid description.";
        Description description = new Description(text);
        assertEquals(description.getText(), text);
    }

    @Test
    void constructor_shouldThrow_whenTextIsBlank() {
        assertThrows(IllegalArgumentException.class, () -> new Description(" "));
    }

    @Test
    void constructor_shouldThrow_whenTextIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new Description(null));
    }

    @Test
    void constructor_shouldThrow_whenTextExceedsMaxLength() {
        String longText = "a".repeat(Description.MAX_LENGTH + 1);
        assertThrows(IllegalArgumentException.class, () -> new Description(longText));
    }

    @Test
    void constructor_shouldAllowTextWithMaxLength() {
        String maxLengthText = "a".repeat(Description.MAX_LENGTH);
        assertDoesNotThrow(() -> new Description(maxLengthText));
    }

}