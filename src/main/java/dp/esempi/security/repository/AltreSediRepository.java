package dp.esempi.security.repository;

import dp.esempi.security.model.AltreSedi;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AltreSediRepository extends JpaRepository<AltreSedi, Integer> {
    List<AltreSedi> findByEmail(String email);
}
