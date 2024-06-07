package dp.esempi.security.repository;

import dp.esempi.security.model.Lingua;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LinguaRepository extends JpaRepository<Lingua, Integer> {
}
