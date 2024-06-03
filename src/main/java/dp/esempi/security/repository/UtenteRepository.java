package dp.esempi.security.repository;

import dp.esempi.security.model.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UtenteRepository extends JpaRepository<Utente, String> {

    Optional<Utente> findByEmail(String email);
    Optional<Utente> findByEmailAndPassword(String email, String password);
}
