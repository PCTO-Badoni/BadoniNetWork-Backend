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

    @Query(value = "SELECT * FROM aziende_approved WHERE email = :email LIMIT 1", nativeQuery = true)
    Optional<AziendaWaiting> findByEmailInAziendeApproved(@Param("email") String email);

    @Query(value = "SELECT * FROM aziende_approved WHERE codice = :codice LIMIT 1", nativeQuery = true)
    Optional<AziendaWaiting> findByCodiceInAziendeApproved(@Param("codice") String codice);
    
    @Query(value = "INSERT INTO aziende_approved (ragionesociale, email, indirizzo, telefono, codice) VALUES (:ragionesociale, :email, :indirizzo, :telefono, :codice)", nativeQuery = true)
    void saveInAziendeApproved(@Param("ragionesociale") String ragionesociale, @Param("email") String email, @Param("indirizzo") String indirizzo, @Param("telefono") String telefono, @Param("codice") String codice);
}
