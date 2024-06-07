package dp.esempi.security.repository;

import dp.esempi.security.model.AziendaWaiting;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AziendaWaitingRepository extends JpaRepository<AziendaWaiting, String> {

    Optional<AziendaWaiting> findByEmail(String email);

    Optional<AziendaWaiting> findByCodice(String codice);
}
