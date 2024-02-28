package prince.aktiveskochbuch.adapter.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import prince.aktiveskochbuch.domain.models.Confirmation;

@Repository
@RepositoryRestResource(collectionResourceRel = "confirmations", path = "confirmations")
public interface ConfirmationRepository extends JpaRepository<Confirmation, Long> {
    Confirmation findByToken(String token);
}
