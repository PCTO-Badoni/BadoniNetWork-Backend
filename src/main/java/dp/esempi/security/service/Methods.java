package dp.esempi.security.service;

import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dp.esempi.security.model.Azienda;
import dp.esempi.security.model.VerificaEmailStudenti;
import dp.esempi.security.repository.AziendaRepository;
import dp.esempi.security.repository.VerificaEmailStudentiRepository;

@Service
public class Methods {

    @Autowired
    private AziendaRepository aziendaRepository;
    @Autowired
    private VerificaEmailStudentiRepository verificaEmailStudentiRepository;
    private Random random = new Random();

    public String generateCode() {
        Optional<Azienda> aziendaFind;
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
    
            aziendaFind = aziendaRepository.findByCodice(codice);
            verificaEmailStudentiFind = verificaEmailStudentiRepository.findByCodice(codice);

        } while (aziendaFind.isPresent() && verificaEmailStudentiFind.isPresent() && contatore < max_tentativi);

        if (contatore>=max_tentativi) {
            return "error";
        }

        return codice;
    }
}
