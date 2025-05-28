package dp.esempi.security.repository;

import dp.esempi.security.model.CompetenzaStudente;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompetenzaStudenteRepository extends JpaRepository<CompetenzaStudente, Integer> {
    List<CompetenzaStudente> findByStudente_email(String email);
}
