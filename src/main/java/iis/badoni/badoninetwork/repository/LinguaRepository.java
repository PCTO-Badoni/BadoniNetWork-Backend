package iis.badoni.badoninetwork.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import iis.badoni.badoninetwork.model.Lingua;

@Repository
public interface LinguaRepository extends JpaRepository<Lingua, Integer> {
}
