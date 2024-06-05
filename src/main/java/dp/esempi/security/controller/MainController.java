package dp.esempi.security.controller;

import dp.esempi.security.model.AziendaWaiting;
import dp.esempi.security.repository.AziendaWaitingRepository;
import dp.esempi.security.service.EmailService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
@RestController
@CrossOrigin
public class MainController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private AziendaWaitingRepository aziendaRepository;
    
    private Random random = new Random();

    @GetMapping("/accept-request/{email}")
    public ResponseEntity<String> acceptRequest(@PathVariable String email) throws MessagingException, IOException {
        Optional<AziendaWaiting> azienda = aziendaRepository.findByEmail(email);
    
        if (azienda.isEmpty()) {
            return ResponseEntity.badRequest().body("{\"message\": \"Azienda non trovata\"}");
        }

        AziendaWaiting aziendadb = azienda.get();

        if (aziendadb.getCodice() != null) {
            return ResponseEntity.badRequest().body("{\"message\": \"Azienda già accettata\"}");
        }
        
        Optional<AziendaWaiting> aziendaFind;
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
        } while (aziendaFind.isPresent() && contatore < max_tentativi);

        if (contatore>=max_tentativi) {
            return ResponseEntity.badRequest().body("{\"message\": \"Errore nella generazione del codice\"}");
        }

        aziendadb.setCodice(codice);
        aziendaRepository.save(aziendadb);
        
        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("codice", codice);
        emailService.sendHtmlMessage(email, "Accettazione account", templateModel, "request-response-template");
        
        return ResponseEntity.ok().body("{\"message\": \"Richiesta accettata");
    }

    @GetMapping("/deny-request/{email}")
    public ResponseEntity<String> denyRequest(@PathVariable String email) throws MessagingException, IOException {
        Optional<AziendaWaiting> azienda = aziendaRepository.findByEmail(email);
    
        if (azienda.isEmpty()) {
            return ResponseEntity.badRequest().body("{\"message\": \"Azienda non trovata\"}");
        }

        AziendaWaiting aziendadb = azienda.get();

        aziendaRepository.delete(aziendadb);

        Map<String, Object> templateModel = new HashMap<>();
        emailService.sendHtmlMessage(email, "Richiesta account Badoni NetWork", templateModel, "request-deny-template");
        
        return ResponseEntity.ok().body("{\"message\": \"Richiesta rifiutata");
    }
}

