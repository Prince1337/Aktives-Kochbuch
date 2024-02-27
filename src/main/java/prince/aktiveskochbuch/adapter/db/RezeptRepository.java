package prince.aktiveskochbuch.adapter.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import prince.aktiveskochbuch.domain.models.Rezept;

import java.util.Optional;

@Repository
public interface RezeptRepository extends JpaRepository<Rezept, Long>{

    Optional<Rezept> findByTitel(String title);
}
