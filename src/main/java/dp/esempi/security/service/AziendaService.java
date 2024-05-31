package dp.esempi.security.service;

import dp.esempi.security.model.Azienda;
import dp.esempi.security.model.Utente;
import dp.esempi.security.repository.AziendaRepository;
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
public class AziendaService implements UserDetailsService {
    @Autowired
    private AziendaRepository aziendaRepository;



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Azienda> azienda = aziendaRepository.findByEmail(username);

        if(azienda.isPresent()) {
            var company = azienda.get();
            return User.builder()
                    .username(company.getRagionesociale())
                    .roles(getRoles(company))
                    .build();
        } else {
            throw new UsernameNotFoundException(username);
        }
    }

    public Optional<Azienda> getBySocialRagion(String ragionesociale) {
        return aziendaRepository.findByragionesociale(ragionesociale);
    }

    public Optional<Azienda> getByEmail(String email) {
        return aziendaRepository.findByEmail(email);

    }

    private String[] getRoles(Azienda azienda) {
        if (azienda.getRole() == null) {
            return new String[]{"USER"};
        }
        return azienda.getRole().split(",");
    }
}
