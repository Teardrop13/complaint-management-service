package pl.teardrop.complaintmanagementservice.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.teardrop.complaintmanagementservice.model.Complaint;
import pl.teardrop.complaintmanagementservice.model.ProductId;
import pl.teardrop.complaintmanagementservice.model.UserId;

import java.util.Optional;

@Repository
public interface ComplaintRepository extends CrudRepository<Complaint, Long> {

    Optional<Complaint> findByUserIdAndProductId(UserId userId, ProductId productId);
}
