package iis.badoni.badoninetwork.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import iis.badoni.badoninetwork.model.Utente;
import iis.badoni.badoninetwork.repository.UtenteRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UtenteService implements UserDetailsService {
    @Autowired
    private UtenteRepository utenteRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Utente> utente = utenteRepository.findByEmail(email);

        if(utente.isPresent()) {
            var user = utente.get();
            return User.builder()
                    .username(user.getEmail())
                    .password(user.getPassword())
                    .roles(user.getRuolo()).build();
        } else {
            throw new UsernameNotFoundException(email);
        }
    }

    public Optional<Utente> findByEmailAndPassword(String email, String rawPassword, PasswordEncoder passwordEncoder) {
        Optional<Utente> user = utenteRepository.findByEmail(email);
        if (user.isPresent() && passwordEncoder.matches(rawPassword, user.get().getPassword())) {
            return user;
        }
        return Optional.empty();
    }

    public Optional<Utente> findByEmail(String email) {
        Optional<Utente> user = utenteRepository.findByEmail(email);
        if (user.isPresent()) {
            return user;
        }
        return Optional.empty();
    }

    public void saveUtente(Utente utente) {
        utenteRepository.save(utente);
    }
}
