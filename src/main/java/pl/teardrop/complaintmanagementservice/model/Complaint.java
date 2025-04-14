package pl.teardrop.complaintmanagementservice.model;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.Validate;
import pl.teardrop.complaintmanagementservice.dto.ComplaintDto;

import java.time.LocalDateTime;

@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "productId", "userId" }) })
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Complaint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "PRODUCT_ID", nullable = false))
    private ProductId productId;

    @Setter
    @Embedded
    @AttributeOverride(name = "text", column = @Column(name = "DESCRIPTION", nullable = false, length = Description.MAX_LENGTH))
    private Description description;

    @Column(nullable = false)
    private LocalDateTime creationDate;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "USER_ID", nullable = false))
    private UserId userId;

    @Embedded
    @AttributeOverride(name = "code", column = @Column(name = "COUNTRY", nullable = false))
    private Country country;

    @Embedded
    @AttributeOverride(name = "count", column = @Column(name = "SUBMISSION_COUNTER", nullable = false))
    private SubmissionCounter submissionCounter;

    public Complaint(ProductId productId, Description description, LocalDateTime creationDate, UserId userId, Country country) {
        this.productId = Validate.notNull(productId, "productId is null");
        this.description = Validate.notNull(description, "description is null");
        this.creationDate = Validate.notNull(creationDate, "creationDate is null");
        this.userId = Validate.notNull(userId, "userId is null");
        this.country = Validate.notNull(country, "country is null");
        this.submissionCounter = new SubmissionCounter();
    }

    public void increaseSubmissionCounter() {
        submissionCounter = submissionCounter.increase();
    }

    public ComplaintDto toDto() {
        return new ComplaintDto(
                id,
                productId.getId(),
                description.getText(),
                creationDate,
                userId.getId(),
                country.getCode(),
                submissionCounter.getCount()
        );
    }
}
