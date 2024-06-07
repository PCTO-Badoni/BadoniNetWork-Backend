package dp.esempi.security.repository;

import dp.esempi.security.model.Contatti;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContattiRepository extends JpaRepository<Contatti, Integer> {
}
