package dp.esempi.security.repository;

import dp.esempi.security.model.LingueStudenti;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LingueStudentiRepository extends JpaRepository<LingueStudenti, Integer> {
    List<LingueStudenti> findByUsername(String email);
}
