package iis.badoni.badoninetwork.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import iis.badoni.badoninetwork.model.VerificaEmailStudente;

import java.util.Optional;

@Repository
public interface VerificaEmailStudenteRepository extends JpaRepository<VerificaEmailStudente, String> {

    Optional<VerificaEmailStudente> findByEmail(String email);
    Optional<VerificaEmailStudente> findByCodice(String codice);
}
