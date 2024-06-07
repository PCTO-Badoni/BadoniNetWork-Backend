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
    private AziendaWaitingRepository aziendaWaitingRepository;
    
    private Random random = new Random();


    @GetMapping("/accept-request/{email}")
    public ResponseEntity<String> acceptRequest(@PathVariable String email) throws MessagingException, IOException {
        Optional<AziendaWaiting> azienda = aziendaWaitingRepository.findByEmail(email);
    
        return dataProcess(azienda, 1);
    }

    @GetMapping("/accept-approved-request/{email}")
    public ResponseEntity<String> acceptApprovedRequest(@PathVariable String email) throws MessagingException, IOException {
        Optional<AziendaWaiting> azienda = aziendaWaitingRepository.findByEmailInAziendeApproved(email);
    
        return dataProcess(azienda, 2);
    }

    @GetMapping("/deny-request/{email}")
    public ResponseEntity<String> denyRequest(@PathVariable String email) throws MessagingException, IOException {
        Optional<AziendaWaiting> azienda = aziendaWaitingRepository.findByEmail(email);
    
        if (azienda.isEmpty()) {
            return ResponseEntity.badRequest().body("{\"message\": \"Azienda non trovata\"}");
        }

        AziendaWaiting aziendadb = azienda.get();

        aziendaWaitingRepository.delete(aziendadb);

        Map<String, Object> templateModel = new HashMap<>();
        emailService.sendHtmlMessage(email, "Richiesta account Badoni NetWork", templateModel, "request-deny-template");
        
        return ResponseEntity.ok().body("{\"message\": \"Richiesta rifiutata");
    }


    private ResponseEntity<String> dataProcess(Optional<AziendaWaiting> azienda, int mode) throws MessagingException, IOException {
        if (azienda.isEmpty()) {
            return ResponseEntity.badRequest().body("{\"message\": \"Azienda non trovata\"}");
        }

        AziendaWaiting aziendadb = azienda.get();

        if (aziendadb.getCodice() != null) {
            return ResponseEntity.badRequest().body("{\"message\": \"Azienda già accettata\"}");
        }

        String codice = generateCode();

        if (codice.equals("error")) {
            return ResponseEntity.badRequest().body("{\"message\": \"Errore nella generazione del codice\"}");
        }

        aziendadb.setCodice(codice);

        if (mode == 1) {
            aziendaWaitingRepository.save(aziendadb);
        } else if (mode == 2) {
            aziendaWaitingRepository.saveInAziendeApproved(aziendadb.getRagionesociale(), aziendadb.getEmail(), aziendadb.getIndirizzo() , aziendadb.getTelefono(), aziendadb.getCodice());
        }

        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("codice", codice);
        emailService.sendHtmlMessage(aziendadb.getEmail(), "Accettazione account", templateModel, "request-response-template");
        
        return ResponseEntity.ok().body("{\"message\": \"Richiesta accettata");
    }

    private String generateCode() {
        Optional<AziendaWaiting> aziendaFind;
        Optional<AziendaWaiting> aziendaApprovedFind;
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
            aziendaApprovedFind = aziendaWaitingRepository.findByCodiceInAziendeApproved(codice);

        } while (aziendaFind.isPresent() && aziendaApprovedFind.isPresent() && contatore < max_tentativi);

        if (contatore>=max_tentativi) {
            return "error";
        }

        return codice;
    }
}

