package iis.badoni.badoninetwork.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import iis.badoni.badoninetwork.model.LinguaStudente;

@Repository
public interface LinguaStudenteRepository extends JpaRepository<LinguaStudente, Integer> {
    List<LinguaStudente> findByStudente_email(String email);
}
