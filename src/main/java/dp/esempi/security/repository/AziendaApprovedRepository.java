package dp.esempi.security.repository;

import dp.esempi.security.model.AziendaApproved;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AziendaApprovedRepository extends JpaRepository<AziendaApproved, String> {

    Optional<AziendaApproved> findByEmail(String email);
    Optional<AziendaApproved> findByCodice(String codice);
}
