package pl.teardrop.complaintmanagementservice.model;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public class SubmissionCounter {

    private int count;

    public SubmissionCounter() {
        count = 1;
    }

    private SubmissionCounter(int count) {
        if (count < 1) {
            throw new IllegalArgumentException("Counter can't be less then 1, value: " + count);
        }
        this.count = count;
    }

    public SubmissionCounter increase() {
        return new SubmissionCounter(count + 1);
    }
}
