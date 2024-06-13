package dp.esempi.security.repository;

import dp.esempi.security.model.VerificaEmailStudenti;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerificaEmailStudentiRepository extends JpaRepository<VerificaEmailStudenti, String> {

    Optional<VerificaEmailStudenti> findByEmail(String email);
    Optional<VerificaEmailStudenti> findByCodice(String codice);
}
