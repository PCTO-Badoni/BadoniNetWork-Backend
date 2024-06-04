package dp.esempi.security.controller;

import dp.esempi.security.model.Azienda;
import dp.esempi.security.model.Utente;
import dp.esempi.security.repository.AziendaRepository;
import dp.esempi.security.repository.UtenteRepository;
import dp.esempi.security.service.EmailService;
import jakarta.mail.MessagingException;
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
    private AziendaRepository aziendaRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private EmailService emailService;

    @PostMapping("/azienda")
    public ResponseEntity<String> createCompany(@Valid Azienda azienda, Errors errors) throws MessagingException, IOException {
        if(errors.hasErrors()) {
            if (errors.getAllErrors().toString().contains("Richiesta già inviata")) {
                return ResponseEntity.badRequest().body("{\"message\": \"Richiesta già inviata\"}");   
                
            } else if (errors.getAllErrors().toString().contains("Account esistente")) {
                return ResponseEntity.badRequest().body("{\"message\": \"Account già esistente\"}");

            } else if (errors.getAllErrors().toString().contains("Account già approvato")) {
                return ResponseEntity.badRequest().body("{\"message\": \"Account già approvato\"}");
            }
        }

        azienda.setRole("USER");
        aziendaRepository.save(azienda);
            
        sendEmail(azienda.getRagionesociale(),azienda.getEmail(),azienda.getTelefono(),azienda.getIndirizzo());
        
        return ResponseEntity.ok("{\"message\": \"Account creato con successo\"}");
    }

    @PostMapping("/utente")
    public ResponseEntity<String> createUser(@Valid Utente utente, Errors errors) {
        if(errors.hasErrors()) {
            return ResponseEntity.badRequest().body("{\"message\": \"Account già esistente\"}");
        }

        utente.setPassword(passwordEncoder.encode(utente.getPassword()));
        utente.setRole("USER");
        utenteRepository.save(utente);
        return ResponseEntity.ok("{\"message\": \"Account creato con successo\"}");
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