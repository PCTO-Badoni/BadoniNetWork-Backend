package dp.esempi.security.repository;

import dp.esempi.security.model.VerificaEmailStudente;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerificaEmailStudenteRepository extends JpaRepository<VerificaEmailStudente, String> {

    Optional<VerificaEmailStudente> findByEmail(String email);
    Optional<VerificaEmailStudente> findByCodice(String codice);
}
