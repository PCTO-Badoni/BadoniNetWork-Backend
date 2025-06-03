package iis.badoni.badoninetwork.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import iis.badoni.badoninetwork.model.Competenza;

@Repository
public interface CompetenzaRepository extends JpaRepository<Competenza, Integer> {
}
