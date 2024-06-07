package dp.esempi.security.repository;

import dp.esempi.security.model.CompetenzeStudenti;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompetenzeStudentiRepository extends JpaRepository<CompetenzeStudenti, Integer> {
}
