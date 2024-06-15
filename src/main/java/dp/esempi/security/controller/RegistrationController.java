package dp.esempi.security.controller;

import dp.esempi.security.model.Azienda;
import dp.esempi.security.model.Disponibile;
import dp.esempi.security.model.TipoAzienda;
import dp.esempi.security.model.Utente;
import dp.esempi.security.repository.AziendaRepository;
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
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;



@RequestMapping("/register")
@RestController
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
    public ResponseEntity<?> createCompany(@RequestBody @Valid Azienda azienda, Errors errors, HttpServletResponse response) throws MessagingException, IOException {
        if(errors.hasErrors()) {
            for (ObjectError error : errors.getAllErrors()) {
                return ResponseEntity.badRequest().body("{\"message\": "+error.getDefaultMessage()+"}");
            }
        }

        //Operazioni per approvare automaticamente l'azienda
        Optional<Azienda> a = aziendaRepository.findByEmail(azienda.getEmail());

        if (a.isPresent()) {
            if (a.get().getType().equals(TipoAzienda.A))
            response.sendRedirect("http://localhost:8080/accept-request/"+azienda.getEmail());
            return ResponseEntity.ok(null);
        }

        azienda.setType(TipoAzienda.W);
        //Operazioni per aggiungere l'azienda al waiting
        aziendaRepository.save(azienda);
            
        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("ragionesociale", azienda.getRagionesociale());
        templateModel.put("email", azienda.getEmail());
        templateModel.put("telefono", azienda.getTelefono());
        templateModel.put("indirizzo", azienda.getIndirizzo());
        templateModel.put("id", azienda.getEmail());

        emailService.sendHtmlMessage("srmndr06p13e507g@iisbadoni.edu.it", "Richiesta account Badoni NetWork", templateModel, "account-request-template");
        
        return ResponseEntity.ok("{\"message\": \"Email inviata\"}");
    }

    @PostMapping("/utente")
    public ResponseEntity<String> createUser(@RequestBody @Valid Utente utente, Errors errors) {
        if(errors.hasErrors()) {
            for (ObjectError error : errors.getAllErrors()) {
                System.out.println(error.getDefaultMessage());
                return ResponseEntity.badRequest().body("{\"message\": "+error.getDefaultMessage()+"}");
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

        Azienda azienda;

        azienda = aziendaRepository.findByCodice(requestBody.get("codice")).orElse(null);

        if (azienda == null) {
            return ResponseEntity.badRequest().body("{\"message\": \"Codice invalido\"}");
        }
        return ResponseEntity.ok(azienda);
    }
    

    @PostMapping("/confirm-azienda")
    public ResponseEntity<String> confirmCompany(@RequestBody Azienda azienda, Errors errors) {

        azienda.setPassword(passwordEncoder.encode(azienda.getPassword()));
        azienda.setType(TipoAzienda.R);
        aziendaRepository.save(azienda);

        return ResponseEntity.ok("{\"message\": \"Account creato\"}");
    }
}