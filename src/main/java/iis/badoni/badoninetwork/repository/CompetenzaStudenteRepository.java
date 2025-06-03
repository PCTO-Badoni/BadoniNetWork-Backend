package iis.badoni.badoninetwork.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import iis.badoni.badoninetwork.model.CompetenzaStudente;

@Repository
public interface CompetenzaStudenteRepository extends JpaRepository<CompetenzaStudente, Integer> {
    List<CompetenzaStudente> findByStudente_email(String email);
}
