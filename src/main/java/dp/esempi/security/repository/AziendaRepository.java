package dp.esempi.security.repository;

import dp.esempi.security.model.Azienda;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AziendaRepository extends JpaRepository<Azienda, String> {

    Optional<Azienda> findByEmail(String email);
}
