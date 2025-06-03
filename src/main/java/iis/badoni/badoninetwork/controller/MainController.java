package iis.badoni.badoninetwork.controller;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import iis.badoni.badoninetwork.model.Azienda;
import iis.badoni.badoninetwork.model.TipoAzienda;
import iis.badoni.badoninetwork.model.Utente;
import iis.badoni.badoninetwork.service.AziendaService;
import iis.badoni.badoninetwork.service.EmailService;
import iis.badoni.badoninetwork.service.Methods;
import iis.badoni.badoninetwork.service.UtenteService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
public class MainController {

    @Value("${backend_address}")
    private String backendAddress;

    @Value("${frontend_address}")
    private String frontendAddress;

    @Autowired
    private EmailService emailService;
    @Autowired
    private AziendaService aziendaService;
    @Autowired
    private UtenteService utenteService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private Methods methods;

    @GetMapping("/accept-request/{email}")
    public String acceptRequest(Model model, @PathVariable String email) throws MessagingException, IOException {
        Optional<Azienda> azienda = aziendaService.findByEmail(email);
        String message = "Richiesta accettata";
    
        if (azienda.isEmpty()) {
            message="Azienda non trovata";
        }
    
        Azienda aziendaget = azienda.get();

        if (aziendaget.getCodice() != null || aziendaget.getType().equals(TipoAzienda.R)) {
            message="Azienda già accettata";
        }
    
        // Genera un nuovo codice
        String codice = methods.generateCode();
    
        if (codice.equals("error")) {
           message="Errore nella generazione del codice";
        }

        aziendaget.setCodice(codice);

        aziendaService.saveAzienda(aziendaget);
    
        // Invia la mail di accettazione
        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("codice", codice);
        templateModel.put("backend_address", backendAddress);
        emailService.sendHtmlMessage(aziendaget.getEmail(), "Accettazione account", templateModel, "request-response-template");
    
        //Imposta le variabili della pagina dinamica
        model.addAttribute("ragionesociale", aziendaget.getRagionesociale());
        model.addAttribute("telefono", aziendaget.getTelefono());
        model.addAttribute("email", aziendaget.getEmail());
        model.addAttribute("message", message);

        return "request-feedback-page";
    }

    @GetMapping("/deny-request/{email}")
    public String denyRequest(Model model, @PathVariable String email) throws MessagingException, IOException {
        Optional<Azienda> azienda = aziendaService.findByEmail(email);
        String message = "Richiesta rifiutata";
    
        if (azienda.isEmpty()) {
            message="Azienda non trovata";
        }

        Azienda aziendadb = azienda.get();

        if (!aziendadb.getType().equals(TipoAzienda.W)) {
            message="Hai già preso provvedimenti per questa richiesta";
        }

        aziendaService.removeAzienda(aziendadb);

        // Invia la mail di accettazione
        Map<String, Object> templateModel = new HashMap<>();
        emailService.sendHtmlMessage(email, "Richiesta account Badoni NetWork", templateModel, "request-deny-template");
        
        //Imposta le variabili della pagina dinamica
        model.addAttribute("ragionesociale", aziendadb.getRagionesociale());
        model.addAttribute("telefono", aziendadb.getTelefono());
        model.addAttribute("email", aziendadb.getEmail());
        model.addAttribute("message", message);

        return "request-feedback-page";
    }

    @PostMapping("/password-recovery")
    public ResponseEntity<String> passwordRecovery(@RequestBody Map<String, String> payload) throws MessagingException, IOException {
        String email = payload.get("email");

        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("email", email);
        templateModel.put("backend_address", backendAddress);

        Optional<Azienda> azienda = aziendaService.findByEmail(email);
        Optional<Utente> studente = utenteService.findByEmail(email);

        if (azienda.isEmpty() && studente.isEmpty()){
            return ResponseEntity.badRequest().body("{\"message\": \"Account non trovato\"}");
        }

        emailService.sendHtmlMessage(email, "Recupero Password", templateModel, "password-recovery-template");

        return ResponseEntity.ok().body("{\"message\": \"Controlla la casella di posta\"}");
    }

    @GetMapping("/password-recovery/{email}")
    public void passwordRecoveryRedirect(@PathVariable String email, HttpServletResponse response) throws IOException {
        // Costruisci l'URL del frontend con il parametro email
        String frontendUrl = "http://localhost:3001/changePassword/" + email;  //!frontendAddress + "/changePassword/" + email

        // Fai il redirect verso il frontend
        response.sendRedirect(frontendUrl);
    }

    @PostMapping("/change-password-recovery")
    public ResponseEntity<String> passwordRecoveryChange(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        String password = payload.get("password");
        String confirmPassword = payload.get("confirmPassword");

        if (!password.equals(confirmPassword)) {
            return ResponseEntity.badRequest().body("{\"message\": \"Le password non corrispondono\"}");
        }

        Optional<Azienda> azienda = aziendaService.findByEmail(email);
        Optional<Utente> studente = utenteService.findByEmail(email);

        if (azienda.isEmpty() && studente.isEmpty()){
            return ResponseEntity.badRequest().body("{\"message\": \"Account non trovato\"}");
        }

        if (studente.isPresent()) {
            Utente utente = studente.get();
            utente.setPassword(passwordEncoder.encode(password));
            utenteService.saveUtente(utente);
        }
        if (azienda.isPresent()) {
            Azienda utente = azienda.get();
            utente.setPassword(passwordEncoder.encode(password));
            aziendaService.saveAzienda(utente);
        }
        
        return ResponseEntity.ok().body("{\"message\": \"Password cambiata\"}");
    }
    

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody Map<String, String> payload) {
        String old_password = payload.get("old_password");
        String new_password = payload.get("new_password");
        String email = payload.get("email");

        Optional<Utente> user = utenteService.findByEmail(email);
        Optional<Azienda> azienda = aziendaService.findByEmail(email);

        if (user.isEmpty() && azienda.isEmpty()) {
            return ResponseEntity.badRequest().body("{\"message\": \"Account inesistente\"}");
        }

        String account_password;
        String regex = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[a-z])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).{8,}$";

        if (user.isPresent()) {
            account_password = user.get().getPassword();

            if (passwordEncoder.matches(old_password, account_password)) {
    
                if (!new_password.matches(regex)) {
                    return ResponseEntity.badRequest().body("{\"message\": \"Le nuove credenziali non sono valide\"}"); 
                }
    
                user.get().setPassword(passwordEncoder.encode(new_password));
                utenteService.saveUtente(user.get());
            } else {
                return ResponseEntity.badRequest().body("{\"message\": \"Credenziali errate\"}"); 
            }
        }

        if (azienda.isPresent()) {
            account_password = azienda.get().getPassword();

            if (passwordEncoder.matches(old_password, account_password)) {
    
                if (!new_password.matches(regex)) {
                    return ResponseEntity.badRequest().body("{\"message\": \"Le nuove credenziali non sono valide\"}"); 
                }

                azienda.get().setPassword(passwordEncoder.encode(new_password));
                aziendaService.saveAzienda(azienda.get());
            } else {
                return ResponseEntity.badRequest().body("{\"message\": \"Credenziali errate\"}"); 
            }
        }

        return ResponseEntity.ok().body("{\"message\": \"Password cambiata\"}");
    }
}
