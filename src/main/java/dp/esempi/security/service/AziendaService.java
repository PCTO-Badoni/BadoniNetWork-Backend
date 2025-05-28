package dp.esempi.security.service;

import dp.esempi.security.model.Azienda;
import dp.esempi.security.repository.AziendaRepository;

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
public class AziendaService implements UserDetailsService {
    @Autowired
    private AziendaRepository aziendaRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Azienda> azienda = aziendaRepository.findByEmail(email);

        if(azienda.isPresent()) {
            var user = azienda.get();
            return User.builder()
                    .username(user.getEmail())
                    .password(user.getPassword())
                    .roles(user.getRuolo()).build();
        } else {
            throw new UsernameNotFoundException(email);
        }
    }

    public Optional<Azienda> findByEmailAndPassword(String email, String rawPassword, PasswordEncoder passwordEncoder) {
        Optional<Azienda> azienda = aziendaRepository.findByEmail(email);
        if (azienda.isPresent() && passwordEncoder.matches(rawPassword, azienda.get().getPassword())) {
            return azienda;
        }
        return Optional.empty();
    }

    public Optional<Azienda> findByEmail(String email) {
        Optional<Azienda> azienda = aziendaRepository.findByEmail(email);
        if (azienda.isPresent()) {
            return azienda;
        }
        return Optional.empty();
    }

    public Optional<Azienda> findByCodice(String codice) {
        Optional<Azienda> azienda = aziendaRepository.findByCodice(codice);
        if (azienda.isPresent()) {
            return azienda;
        }
        return Optional.empty();
    }

    public void saveAzienda(Azienda azienda) {
        aziendaRepository.save(azienda);
    }

    public void removeAzienda(Azienda azienda) {
        aziendaRepository.delete(azienda);
    }
}
