package dp.esempi.security.repository;

import dp.esempi.security.model.LivelloCompetenze;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LivelloCompentezeRepository extends JpaRepository<LivelloCompetenze, String> {
}
