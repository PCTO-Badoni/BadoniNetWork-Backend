package dp.esempi.security.service;

import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dp.esempi.security.model.AziendaApproved;
import dp.esempi.security.model.AziendaWaiting;
import dp.esempi.security.model.VerificaEmailStudenti;
import dp.esempi.security.repository.AziendaApprovedRepository;
import dp.esempi.security.repository.AziendaWaitingRepository;
import dp.esempi.security.repository.VerificaEmailStudentiRepository;

@Service
public class Methods {
    @Autowired
    private AziendaWaitingRepository aziendaWaitingRepository;
    @Autowired
    private AziendaApprovedRepository aziendaApprovedRepository;
    @Autowired
    private VerificaEmailStudentiRepository verificaEmailStudentiRepository;
    private Random random = new Random();

    public String generateCode() {
        Optional<AziendaWaiting> aziendaFind;
        Optional<AziendaApproved> aziendaApprovedFind;
        Optional<VerificaEmailStudenti> verificaEmailStudentiFind;
        String codice;
        int contatore = 0;
        int max_tentativi = 1000;

        do {
            contatore++;

            int min = 100000;
            int max = 999999;
            int randomNumber = min + random.nextInt(max - min + 1);
            codice = String.valueOf(randomNumber);
    
            aziendaFind = aziendaWaitingRepository.findByCodice(codice);
            aziendaApprovedFind = aziendaApprovedRepository.findByCodice(codice);
            verificaEmailStudentiFind = verificaEmailStudentiRepository.findByCodice(codice);

        } while (aziendaFind.isPresent() && aziendaApprovedFind.isPresent() && verificaEmailStudentiFind.isPresent() && contatore < max_tentativi);

        if (contatore>=max_tentativi) {
            return "error";
        }

        return codice;
    }
}
