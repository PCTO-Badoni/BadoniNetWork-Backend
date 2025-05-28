package dp.esempi.security.repository;

import dp.esempi.security.model.LinguaStudente;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LinguaStudenteRepository extends JpaRepository<LinguaStudente, Integer> {
    List<LinguaStudente> findByStudente_email(String email);
}
