package dp.esempi.security.repository;

import dp.esempi.security.model.Competenza;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompetenzaRepository extends JpaRepository<Competenza, Integer> {
}
