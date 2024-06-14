package dp.esempi.security.repository;

import dp.esempi.security.model.Azienda;
import dp.esempi.security.model.TipoAzienda;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AziendaRepository extends JpaRepository<Azienda, String> {

    Optional<Azienda> findByCodice(String codice);

    Optional<Azienda> findByEmail(String email);

    List<Azienda> findByType(TipoAzienda type);
}
