package dp.esempi.security.service;

import dp.esempi.security.model.AziendaWaiting;
import dp.esempi.security.repository.AziendaWaitingRepository;
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
public class AziendaWaitingService implements UserDetailsService {
    @Autowired
    private AziendaWaitingRepository aziendaRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<AziendaWaiting> azienda = aziendaRepository.findByEmail(username);

        if(azienda.isPresent()) {
            var company = azienda.get();
            return User.builder()
                    .username(company.getRagionesociale())
                    .build();
        } else {
            throw new UsernameNotFoundException(username);
        }
    }

    public Optional<AziendaWaiting> getByEmail(String email) {
        return aziendaRepository.findByEmail(email);

    }
}
