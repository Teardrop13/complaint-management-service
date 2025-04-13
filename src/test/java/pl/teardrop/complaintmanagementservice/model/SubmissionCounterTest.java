package pl.teardrop.complaintmanagementservice.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SubmissionCounterTest {

    @Test
    void increase_shouldIncreaseCounterByOne() {
        SubmissionCounter counter = new SubmissionCounter();
        SubmissionCounter increased = counter.increase();

        assertEquals(1, counter.getCount());
        assertEquals(2, increased.getCount());
    }
}