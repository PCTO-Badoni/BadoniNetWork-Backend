package dp.esempi.security.service;

import dp.esempi.security.model.Utente;
import dp.esempi.security.repository.UtenteRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
                    .roles(getRoles(user)).build();

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

    private String[] getRoles(Utente user) {
        if (user.getRuolo() == null) {
            return new String[]{"USER"};
        }
        return user.getRuolo().split(",");
    }
}
