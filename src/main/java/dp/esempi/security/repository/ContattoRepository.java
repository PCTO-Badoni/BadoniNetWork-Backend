package dp.esempi.security.repository;

import dp.esempi.security.model.Contatto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContattoRepository extends JpaRepository<Contatto, Integer> {
}
