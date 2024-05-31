package dp.esempi.security.service;

import dp.esempi.security.model.Utente;
import dp.esempi.security.repository.UtenteRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UtenteService implements UserDetailsService {
    @Autowired
    private UtenteRepository utenteRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Utente> utente = utenteRepository.findByUsername(username);

        if(utente.isPresent()) {
            var user = utente.get();
            return User.builder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .roles(getRoles(user))
                    .build();
        } else {
            throw new UsernameNotFoundException(username);
        }
    }

    public Optional<Utente> getByUsername(String username) {
        return utenteRepository.findByUsername(username);

    }

    public Optional<Utente> getByEmail(String email) {
        return utenteRepository.findByEmail(email);

    }

    private String[] getRoles(Utente user) {
        if (user.getRole() == null) {
            return new String[]{"USER"};
        }
        return user.getRole().split(",");
    }
}
