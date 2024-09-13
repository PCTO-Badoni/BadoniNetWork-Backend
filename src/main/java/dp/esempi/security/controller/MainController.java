package dp.esempi.security.controller;

import dp.esempi.security.model.Azienda;
import dp.esempi.security.model.TipoAzienda;
import dp.esempi.security.model.Utente;
import dp.esempi.security.repository.AziendaRepository;
import dp.esempi.security.repository.UtenteRepository;
import dp.esempi.security.service.EmailService;
import dp.esempi.security.service.Methods;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
public class MainController {

    @Autowired
    private EmailService emailService;
    @Autowired
    private AziendaRepository aziendaRepository;
    @Autowired
    private UtenteRepository utenteRepository;

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

    @PostMapping("/password-recovery")
    public ResponseEntity<String> passwordRecovery(@RequestBody Map<String, String> payload) throws MessagingException, IOException {
        String email = payload.get("email");

        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("email", email);

        Optional<Azienda> azienda = aziendaRepository.findByEmail(email);
        Optional<Utente> studente = utenteRepository.findByEmail(email);

        if (azienda.isEmpty() && studente.isEmpty()){
            return ResponseEntity.badRequest().body("{\"message\": \"Account non trovato\"}");
        }

        emailService.sendHtmlMessage(email, "Recupero Password", templateModel, "password-recovery-template");

        return ResponseEntity.ok().body("{\"message\": \"Controlla la casella di posta\"}");
    }

    @GetMapping("/password-recovery/{email}")
    public void passwordRecoveryRedirect(@PathVariable String email, HttpServletResponse response) throws IOException {
        // Costruisci l'URL del frontend con il parametro email
        String frontendUrl = "http://127.0.0.1:3001/recoveryPassword/" + email;  //!DA CAMBIARE CON IL SITO UFFICIALE

        // Fai il redirect verso il frontend
        response.sendRedirect(frontendUrl);
    }

    @PostMapping("/change-password-recovery")
    public ResponseEntity<String> passwordRecoveryChange(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        String password = payload.get("password");

        Optional<Azienda> azienda = aziendaRepository.findByEmail(email);
        Optional<Utente> studente = utenteRepository.findByEmail(email);

        if (azienda.isEmpty() && studente.isEmpty()){
            return ResponseEntity.badRequest().body("{\"message\": \"Account non trovato\"}");
        }

        if (studente.isPresent()) {
            Utente utente = studente.get();
            utente.setPassword(password);
            utenteRepository.save(utente);
        }
        if (azienda.isPresent()) {
            Azienda utente = azienda.get();
            utente.setPassword(password);
            aziendaRepository.save(utente);
        }
        
        return ResponseEntity.ok().body("{\"message\": \"Password cambiata\"}");
    }
    

    // @PostMapping("/change-password")
    // public ResponseEntity<String> changePassword(@RequestBody Map<String, String> payload) {
    //     String old_password = payload.get("email");
    //     String new_password = payload.get("email");
    //     String email = payload.get("email");

    //     return ResponseEntity.ok().body("{\"message\": \"Password cambiata\"}");
    // }
}

