package iis.badoni.badoninetwork.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import iis.badoni.badoninetwork.model.Annuncio;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnnuncioRepository extends JpaRepository<Annuncio, Integer> {

    Optional<Annuncio> findById(int id);

    List<Annuncio> findByAzienda_email(String email);
}
