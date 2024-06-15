package dp.esempi.security.controller;

import dp.esempi.security.model.*;
import dp.esempi.security.repository.*;
import dp.esempi.security.service.EmailService;
import dp.esempi.security.service.Methods;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    private AziendaRepository aziendaRepository;

    @Autowired
    private UtenteRepository utenteRepository;

    @Autowired
    private CambioPasswordRepository cambioPasswordRepository;

    @Autowired
    private Methods methods;
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

    @PostMapping("/password-recovery")
    public ResponseEntity<String> passwordRecovery(@RequestBody Map<String, String> payload) throws MessagingException, IOException {
        String email = payload.get("email");
        Map<String, Object> templateModel = new HashMap<>();

        Optional<Azienda> azienda = aziendaRepository.findByEmail(email);
        Optional<Utente> studente = utenteRepository.findByEmail(email);
        Optional<CambioPassword> utente = cambioPasswordRepository.findByEmail(email);

        if (azienda.isEmpty() && studente.isEmpty()){
            return ResponseEntity.badRequest().body("{\"message\": \"Account non trovato\"}");
        }
        if(utente.isPresent()){
            return ResponseEntity.badRequest().body("{\"message\": \"Richiesta già inviata\"}");
        }
        emailService.sendHtmlMessage(email, "Recupero Password", templateModel, "password-recovery-template");

        String codice;

        do{
            int min = 100000;
            int max = 999999;
            int randomNumber = min + random.nextInt(max - min + 1);
            codice = String.valueOf(randomNumber);
            utente = cambioPasswordRepository.findByCodice(codice);
        } while(!utente.isEmpty());

        CambioPassword cp = new CambioPassword();

        cp.setEmail(email);
        cp.setCodice(codice);

        cambioPasswordRepository.save(cp);

        return ResponseEntity.ok().body("{\"message\": \"Controlla la casella di posta\"}");
    }

    /*@PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody Map<String, String> payload) {

        //Optional<CambioPassword> utente = cambioPasswordRepository.findByCodice();
    }*/

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

