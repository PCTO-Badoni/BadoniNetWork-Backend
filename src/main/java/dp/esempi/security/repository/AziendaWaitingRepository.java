package dp.esempi.security.repository;

import dp.esempi.security.model.AziendaWaiting;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AziendaWaitingRepository extends JpaRepository<AziendaWaiting, String> {

    Optional<AziendaWaiting> findByEmail(String email);

    Optional<AziendaWaiting> findByCodice(String codice);

    @Query(value = "SELECT COUNT(*) FROM azienda WHERE email = :email", nativeQuery = true)
    long countByEmailInAzienda(@Param("email") String email);

    @Query(value = "SELECT COUNT(*) FROM aziende_approved WHERE email = :email", nativeQuery = true)
    long countByEmailInAziendeApproved(@Param("email") String email);
}
