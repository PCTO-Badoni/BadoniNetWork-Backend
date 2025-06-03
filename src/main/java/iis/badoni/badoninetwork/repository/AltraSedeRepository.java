package iis.badoni.badoninetwork.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import iis.badoni.badoninetwork.model.AltraSede;

@Repository
public interface AltraSedeRepository extends JpaRepository<AltraSede, Integer> {
    List<AltraSede> findByAzienda_email(String email);
}
