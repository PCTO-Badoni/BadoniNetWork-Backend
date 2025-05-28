package dp.esempi.security.repository;

import dp.esempi.security.model.Annuncio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AnnuncioRepository extends JpaRepository<Annuncio, Integer> {

    Optional<Annuncio> findById(String id);
}
