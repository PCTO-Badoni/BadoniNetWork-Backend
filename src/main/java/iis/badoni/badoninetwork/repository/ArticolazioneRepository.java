package iis.badoni.badoninetwork.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import iis.badoni.badoninetwork.model.Articolazione;

@Repository
public interface ArticolazioneRepository extends JpaRepository<Articolazione, String> {
}
