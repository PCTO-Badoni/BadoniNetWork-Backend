package dp.esempi.security.controller;

import dp.esempi.security.model.AziendaApproved;
import dp.esempi.security.model.AziendaBase;
import dp.esempi.security.model.AziendaWaiting;
import dp.esempi.security.repository.AziendaApprovedRepository;
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

    @Autowired
    private AziendaApprovedRepository aziendaApprovedRepository;
    
    private Random random = new Random();


    @GetMapping("/accept-request/{email}")
    public ResponseEntity<String> acceptRequest(@PathVariable String email) throws MessagingException, IOException {
        Optional<AziendaWaiting> azienda = aziendaWaitingRepository.findByEmail(email);
    
        return dataProcess(azienda);
    }

    @GetMapping("/accept-approved-request/{email}")
    public ResponseEntity<String> acceptApprovedRequest(@PathVariable String email) throws MessagingException, IOException {
        Optional<AziendaApproved> azienda = aziendaApprovedRepository.findByEmail(email);
    
        return dataProcess(azienda);
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


    private ResponseEntity<String> dataProcess(Optional<? extends AziendaBase> azienda) throws MessagingException, IOException {
        if (azienda.isEmpty()) {
            return ResponseEntity.badRequest().body("{\"message\": \"Azienda non trovata\"}");
        }
    
        AziendaBase aziendaget = azienda.get();

        boolean already_accepted = false;
    
        // Verifica se l'azienda è già stata accettata
        if (aziendaget instanceof AziendaWaiting) {
            AziendaWaiting aziendawaiting = (AziendaWaiting) aziendaget;
    
            if (aziendawaiting.getCodice() != null) {
                already_accepted = true;
            }
    
        } else if (aziendaget instanceof AziendaApproved) {
            AziendaApproved aziendaapproved = (AziendaApproved) aziendaget;
    
            if (aziendaapproved.getCodice() != null) {
                already_accepted = true;
            }

        } else {
            return ResponseEntity.badRequest().body("{\"message\": \"Tipo di azienda non valido\"}");
        }
    
        // Se l'azienda è già stata accettata, restituisci un errore
        if (already_accepted) {
            return ResponseEntity.badRequest().body("{\"message\": \"Azienda già accettata\"}");
        }
    
        // Genera un nuovo codice
        String codice = generateCode();
    
        if (codice.equals("error")) {
            return ResponseEntity.badRequest().body("{\"message\": \"Errore nella generazione del codice\"}");
        }

        if (aziendaget instanceof AziendaWaiting) {
            AziendaWaiting aziendawaiting = (AziendaWaiting) aziendaget;
    
            aziendawaiting.setCodice(codice);
    
        } else if (aziendaget instanceof AziendaApproved) {
            AziendaApproved aziendaapproved = (AziendaApproved) aziendaget;
            
            aziendaapproved.setCodice(codice);

        } else {
            return ResponseEntity.badRequest().body("{\"message\": \"Tipo di azienda non valido\"}");
        }

        if (aziendaget instanceof AziendaWaiting) {
            aziendaWaitingRepository.save((AziendaWaiting) aziendaget);
        } else if (aziendaget instanceof AziendaApproved) {
            aziendaApprovedRepository.save((AziendaApproved) aziendaget);
        }
    
        // Invia la mail di accettazione
        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("codice", codice);
        emailService.sendHtmlMessage(aziendaget.getEmail(), "Accettazione account", templateModel, "request-response-template");
    
        return ResponseEntity.ok().body("{\"message\": \"Richiesta accettata");
    }
    

    private String generateCode() {
        Optional<AziendaWaiting> aziendaFind;
        Optional<AziendaApproved> aziendaApprovedFind;
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

        } while (aziendaFind.isPresent() && aziendaApprovedFind.isPresent() && contatore < max_tentativi);

        if (contatore>=max_tentativi) {
            return "error";
        }

        return codice;
    }
}

