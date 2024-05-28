package dp.esempi.security.service;

import dp.esempi.security.model.Utente;
import dp.esempi.security.repository.UtenteRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;


import java.util.Optional;

@Service
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

    public Utente getByEmail(@PathVariable String email) {
        Optional<Utente> utente = utenteRepository.findByEmail(email);
        if (utente.isPresent()) {
            return utente.get();
        } else {
            throw new UsernameNotFoundException(email);
        }
    }

    private String[] getRoles(Utente user) {
        if (user.getRole() == null) {
            return new String[]{"USER"};
        }
        return user.getRole().split(",");
    }

    public Utente getByUsername(@PathVariable String username) {
        Optional<Utente> utente = utenteRepository.findByUsername(username);
        if (utente.isPresent()) {
            System.out.println("sisi esiste");
            return utente.get();
        } else {
            System.out.println("nono esiste");
            return null;
        }
    }
}
