package dp.esempi.security.repository;

import dp.esempi.security.model.Azienda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AziendaRepository extends JpaRepository<Azienda, String> {

    Optional<Azienda> findByEmail(String email);

    @Query(value = "SELECT COUNT(*) FROM azienda WHERE email = :email", nativeQuery = true)
    long countByEmailInAzienda(@Param("email") String email);

    @Query(value = "SELECT COUNT(*) FROM aziende_approved WHERE email = :email", nativeQuery = true)
    long countByEmailInAziendeApproved(@Param("email") String email);
}
