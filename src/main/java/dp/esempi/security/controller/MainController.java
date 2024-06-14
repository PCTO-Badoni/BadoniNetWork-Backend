package dp.esempi.security.controller;

import dp.esempi.security.model.Azienda;
import dp.esempi.security.model.TipoAzienda;
import dp.esempi.security.repository.AziendaRepository;
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
    private AziendaRepository aziendaRepository;

    @Autowired
    private Methods methods;


    @GetMapping("/accept-request/{email}")
    public ResponseEntity<String> acceptRequest(@PathVariable String email) throws MessagingException, IOException {
        Optional<Azienda> azienda = aziendaRepository.findByEmail(email);
    
        if (azienda.isEmpty()) {
            return ResponseEntity.badRequest().body("{\"message\": \"Azienda non trovata\"}");
        }
    
        Azienda aziendaget = azienda.get();

        if (aziendaget.getCodice() != null || aziendaget.getType().equals(TipoAzienda.R)) {
            return ResponseEntity.badRequest().body("{\"message\": \"Azienda già accettata\"}");
        }
    
        // Genera un nuovo codice
        String codice = methods.generateCode();
    
        if (codice.equals("error")) {
            return ResponseEntity.badRequest().body("{\"message\": \"Errore nella generazione del codice\"}");
        }

        aziendaget.setCodice(codice);

        aziendaRepository.save(aziendaget);
    
        // Invia la mail di accettazione
        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("codice", codice);
        emailService.sendHtmlMessage(aziendaget.getEmail(), "Accettazione account", templateModel, "request-response-template");
    
        return ResponseEntity.ok().body("{\"message\": \"Richiesta accettata");
    }

    @GetMapping("/deny-request/{email}")
    public ResponseEntity<String> denyRequest(@PathVariable String email) throws MessagingException, IOException {
        Optional<Azienda> azienda = aziendaRepository.findByEmail(email);
    
        if (azienda.isEmpty()) {
            return ResponseEntity.badRequest().body("{\"message\": \"Azienda non trovata\"}");
        }

        Azienda aziendadb = azienda.get();

        if (!aziendadb.getType().equals(TipoAzienda.W)) {
            return ResponseEntity.badRequest().body("{\"message\": \"Hai già preso provvedimenti per questa richiesta\"}");
        }

        aziendaRepository.delete(aziendadb);

        Map<String, Object> templateModel = new HashMap<>();
        emailService.sendHtmlMessage(email, "Richiesta account Badoni NetWork", templateModel, "request-deny-template");
        
        return ResponseEntity.ok().body("{\"message\": \"Richiesta rifiutata");
    }
}

