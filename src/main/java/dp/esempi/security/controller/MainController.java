package dp.esempi.security.controller;

import dp.esempi.security.model.AziendaApproved;
import dp.esempi.security.model.AziendaPending;
import dp.esempi.security.model.AziendaWaiting;
import dp.esempi.security.repository.AziendaApprovedRepository;
import dp.esempi.security.repository.AziendaWaitingRepository;
import dp.esempi.security.service.EmailService;
import dp.esempi.security.service.Methods;
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
@RestController
@CrossOrigin
public class MainController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private AziendaWaitingRepository aziendaWaitingRepository;

    @Autowired
    private AziendaApprovedRepository aziendaApprovedRepository;

    @Autowired
    private Methods methods;


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


    private ResponseEntity<String> dataProcess(Optional<? extends AziendaPending> azienda) throws MessagingException, IOException {
        if (azienda.isEmpty()) {
            return ResponseEntity.badRequest().body("{\"message\": \"Azienda non trovata\"}");
        }
    
        AziendaPending aziendaget = azienda.get();

        if (aziendaget.getCodice() != null) {
            return ResponseEntity.badRequest().body("{\"message\": \"Azienda già accettata\"}");
        }
    
        // Genera un nuovo codice
        String codice = methods.generateCode();
    
        if (codice.equals("error")) {
            return ResponseEntity.badRequest().body("{\"message\": \"Errore nella generazione del codice\"}");
        }

        aziendaget.setCodice(codice);

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

}

