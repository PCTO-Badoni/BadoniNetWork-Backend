package dp.esempi.security.repository;

import dp.esempi.security.model.LingueStudenti;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LingueStudentiRepository extends JpaRepository<LingueStudenti, Integer> {
}
