package dp.esempi.security.repository;

import dp.esempi.security.model.Articolazione;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticolazioneRepository extends JpaRepository<Articolazione, String> {
}
