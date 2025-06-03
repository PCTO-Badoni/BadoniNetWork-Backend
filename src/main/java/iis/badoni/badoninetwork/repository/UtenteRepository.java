package iis.badoni.badoninetwork.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import iis.badoni.badoninetwork.model.Utente;

import java.util.Optional;
@Repository
public interface UtenteRepository extends JpaRepository<Utente, String> {

    Optional<Utente> findByEmail(String email);
    Optional<Utente> findByEmailAndPassword(String email, String password);
}
