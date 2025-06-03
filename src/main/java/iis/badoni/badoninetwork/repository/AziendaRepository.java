package iis.badoni.badoninetwork.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import iis.badoni.badoninetwork.model.Azienda;
import iis.badoni.badoninetwork.model.TipoAzienda;

import java.util.List;
import java.util.Optional;

@Repository
public interface AziendaRepository extends JpaRepository<Azienda, String> {

    Optional<Azienda> findByCodice(String codice);

    Optional<Azienda> findByEmail(String email);

    List<Azienda> findByType(TipoAzienda type);
}
