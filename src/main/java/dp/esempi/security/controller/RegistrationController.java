package dp.esempi.security.controller;

import dp.esempi.security.model.Azienda;
import dp.esempi.security.model.AziendaPending;
import dp.esempi.security.model.AziendaWaiting;
import dp.esempi.security.model.Disponibile;
import dp.esempi.security.model.Utente;
import dp.esempi.security.repository.AziendaApprovedRepository;
import dp.esempi.security.repository.AziendaRepository;
import dp.esempi.security.repository.AziendaWaitingRepository;
import dp.esempi.security.repository.UtenteRepository;
import dp.esempi.security.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;



@RequestMapping("/register")
@RestController
@CrossOrigin
public class RegistrationController {
    @Autowired
    private UtenteRepository utenteRepository;
    @Autowired
    private AziendaWaitingRepository aziendaWaitingRepository;
    @Autowired
    private AziendaApprovedRepository aziendaApprovedRepository;
    @Autowired
    private AziendaRepository aziendaRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private EmailService emailService;

    @PostMapping("/azienda")
    public ResponseEntity<?> createCompany(@RequestBody @Valid AziendaWaiting azienda, Errors errors, HttpServletResponse response) throws MessagingException, IOException {
        if(errors.hasErrors()) {
            if (errors.getAllErrors().toString().contains("Richiesta già inviata")) {
                return ResponseEntity.badRequest().body("{\"message\": \"Richiesta già inviata\"}");   
                
            } else if (errors.getAllErrors().toString().contains("Account esistente")) {
                return ResponseEntity.badRequest().body("{\"message\": \"Account già esistente\"}");

            } else if (errors.getAllErrors().toString().contains("Telefono invalido")) {
                return ResponseEntity.badRequest().body("{\"message\": \"Numero di telefono non valido\"}");
            }
        }

        //Operazioni per approvare automaticamente l'azienda
        if (aziendaApprovedRepository.findByEmail(azienda.getEmail()).isPresent()) {
            response.sendRedirect("http://localhost:8080/accept-approved-request/"+azienda.getEmail());
            return ResponseEntity.ok(null);
        }

        //Operazioni per aggiungere l'azienda al waiting
        aziendaWaitingRepository.save(azienda);
            
        sendEmail(azienda.getRagionesociale(),azienda.getEmail(),azienda.getTelefono(),azienda.getIndirizzo());
        
        return ResponseEntity.ok("{\"message\": \"Email inviata\"}");
    }

    @PostMapping("/utente")
    public ResponseEntity<String> createUser(@RequestBody @Valid Utente utente, Errors errors) {
        if(errors.hasErrors()) {
            if (errors.getAllErrors().toString().contains("Nome invalido")) {
                return ResponseEntity.badRequest().body("{\"message\": \"Nome non valido\"}");

            } else if (errors.getAllErrors().toString().contains("Cognome invalido")) {
                return ResponseEntity.badRequest().body("{\"message\": \"Cognome non valido\"}");
                
            } else if (errors.getAllErrors().toString().contains("Email invalida")) {
                return ResponseEntity.badRequest().body("{\"message\": \"Email non valida\"}");
                
            } else if (errors.getAllErrors().toString().contains("Email già esistente")) {
                return ResponseEntity.badRequest().body("{\"message\": \"Account già esistente\"}");
                
            } else if (errors.getAllErrors().toString().contains("Password insicura")) {
                return ResponseEntity.badRequest().body("{\"message\": \"Password insicura\"}");

            } else if (errors.getAllErrors().toString().contains("Email inesistente nella validazione")) {
                return ResponseEntity.badRequest().body("{\"message\": \"Email inesistente nella validazione\"}");
                
            } else if (errors.getAllErrors().toString().contains("Email non verificata")) {
                return ResponseEntity.badRequest().body("{\"message\": \"Email non verificata\"}");
            }
        }

        utente.setPassword(passwordEncoder.encode(utente.getPassword()));
        utente.setRuolo("USER");
        utente.setDisponibile(Disponibile.Y);
        utenteRepository.save(utente);
        
        return ResponseEntity.ok("{\"message\": \"Account creato con successo\"}");
    }

    @GetMapping("/validate-otp")
    public void validateOTP(HttpServletResponse response) throws IOException {
        response.sendRedirect("http://localhost:3001/otp");
    }

    @PostMapping("/validate-otp")
    public ResponseEntity<?> validateOTP(HttpSession httpSession, @RequestBody Map<String, String> requestBody) {
        int tries=-1;

        if (httpSession.getAttribute("tries") == null) {
            httpSession.setAttribute("tries", 1);
        } else {
            tries = (int) httpSession.getAttribute("tries")+1;
            httpSession.setAttribute("tries", tries);
        }

        if (tries>=5) {
            httpSession.removeAttribute("tries");
            //! Implementare un meccanismo
            return ResponseEntity.badRequest().body("{\"message\": \"Tentativi esauriti\"}");
        }

        AziendaPending azienda;

        azienda = aziendaWaitingRepository.findByCodice(requestBody.get("codice")).orElse(null);

        if (azienda == null) {
            azienda = aziendaApprovedRepository.findByCodice(requestBody.get("codice")).orElse(null);

            if (azienda == null) {
                return ResponseEntity.badRequest().body("{\"message\": \"Codice invalido\"}");
            }
        }

        return ResponseEntity.ok(azienda);
    }
    

    @PostMapping("/confirm-azienda")
    public ResponseEntity<String> confirmCompany(@RequestBody Azienda azienda, Errors errors) {

        aziendaRepository.save(azienda);

        return ResponseEntity.ok("{\"message\": \"Account creato\"}");
    }

    private void sendEmail(String ragionesociale, String email, String telefono,String indirizzo) throws MessagingException, IOException {

        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("ragionesociale", ragionesociale);
        templateModel.put("email", email);
        templateModel.put("telefono", telefono);
        templateModel.put("indirizzo", indirizzo);
        templateModel.put("id", email);

        emailService.sendHtmlMessage("srmndr06p13e507g@iisbadoni.edu.it", "Richiesta account Badoni NetWork", templateModel, "account-request-template");
    }
}