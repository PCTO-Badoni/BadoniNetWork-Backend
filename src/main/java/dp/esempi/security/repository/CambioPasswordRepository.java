package dp.esempi.security.repository;

import dp.esempi.security.model.CambioPassword;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CambioPasswordRepository extends JpaRepository<CambioPassword, Integer> {

    Optional<CambioPassword> findByEmail(String email);
    Optional<CambioPassword> findByCodice(String codice);
}
