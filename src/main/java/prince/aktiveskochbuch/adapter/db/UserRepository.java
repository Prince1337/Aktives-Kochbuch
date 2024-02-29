package prince.aktiveskochbuch.adapter.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import prince.aktiveskochbuch.domain.models.User;

import java.util.Optional;

@Repository
@RepositoryRestResource(collectionResourceRel = "users", path = "users")
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    User findByEmailIgnoreCase(String email);
    Boolean existsByEmail(String email);

}