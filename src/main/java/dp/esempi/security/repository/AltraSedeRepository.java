package dp.esempi.security.repository;

import dp.esempi.security.model.AltraSede;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AltraSedeRepository extends JpaRepository<AltraSede, Integer> {
    List<AltraSede> findByAzienda_email(String email);
}
