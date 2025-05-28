package dp.esempi.security.repository;

import dp.esempi.security.model.LivelloCompetenza;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LivelloCompentezaRepository extends JpaRepository<LivelloCompetenza, String> {
}
