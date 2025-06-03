package iis.badoni.badoninetwork.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import iis.badoni.badoninetwork.model.LivelloCompetenza;

@Repository
public interface LivelloCompentezaRepository extends JpaRepository<LivelloCompetenza, String> {
}
