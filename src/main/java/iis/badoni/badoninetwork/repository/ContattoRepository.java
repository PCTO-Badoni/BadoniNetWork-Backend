package iis.badoni.badoninetwork.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import iis.badoni.badoninetwork.model.Contatto;

@Repository
public interface ContattoRepository extends JpaRepository<Contatto, Integer> {
}
