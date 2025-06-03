package iis.badoni.badoninetwork.service;

import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import iis.badoni.badoninetwork.model.Azienda;
import iis.badoni.badoninetwork.model.VerificaEmailStudente;
import iis.badoni.badoninetwork.repository.AziendaRepository;
import iis.badoni.badoninetwork.repository.VerificaEmailStudenteRepository;

@Service
public class Methods {

    @Autowired
    private AziendaRepository aziendaRepository;
    @Autowired
    private VerificaEmailStudenteRepository verificaEmailStudentiRepository;
    private Random random = new Random();

    public String generateCode() {
        Optional<Azienda> aziendaFind;
        Optional<VerificaEmailStudente> verificaEmailStudentiFind;
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
