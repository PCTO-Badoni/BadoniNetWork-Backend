package dp.esempi.security.controller;

import dp.esempi.security.model.Utente;
import dp.esempi.security.model.Azienda;
import dp.esempi.security.model.Disponibile;
import dp.esempi.security.model.TipoAzienda;
import dp.esempi.security.service.AziendaService;
import dp.esempi.security.service.EmailService;
import dp.esempi.security.service.UtenteService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RestController
public class AuthController {

    @Value("${backend_address}")
    private String backendAddress;

    @Value("${frontend_address}")
    private String frontendAddress;

    @Value("${staff_email}")
    private String staffEmail;

    @Autowired
    private UtenteService utenteService;
    @Autowired
    private AziendaService aziendaService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private EmailService emailService;
    @Autowired
    private MainController mainController;
    
    @PostMapping("/login")
    public ResponseEntity<?> loginEntity(@RequestBody Map<String, String> payload, HttpSession httpSession, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        String email = payload.get("email");
        String password = payload.get("password");

        Optional<Utente> user = utenteService.findByEmailAndPassword(email, password, passwordEncoder);
        Optional<Azienda> company = aziendaService.findByEmailAndPassword(email, password, passwordEncoder);

        if(user.isPresent()) {
            httpSession.setAttribute("user-account", user.get());
            return ResponseEntity.ok().body(user.get());
        }

        if (company.isPresent()) {
            httpSession.setAttribute("user-account", company.get());
            return ResponseEntity.ok().body(company.get());
        }

        return ResponseEntity.badRequest().body("{\"message\": \"Credenziali errate\"}");
    }

    @PostMapping("/register/azienda")
    public ResponseEntity<?> createCompany(@RequestBody @Valid Azienda azienda, Errors errors, HttpServletResponse response) throws MessagingException, IOException {
        if(errors.hasErrors()) {
            for (ObjectError error : errors.getAllErrors()) {
                return ResponseEntity.badRequest().body("{\"message\": "+error.getDefaultMessage()+"}");
            }
        }

        //Operazioni per approvare automaticamente l'azienda
        Optional<Azienda> a = aziendaService.findByEmail(azienda.getEmail());

        if (a.isPresent()) {
            if (a.get().getType().equals(TipoAzienda.A))
            mainController.acceptRequest(null,azienda.getEmail());
            return ResponseEntity.ok(null);
        }

        azienda.setType(TipoAzienda.W);
        //Operazioni per aggiungere l'azienda al waiting
        aziendaService.saveAzienda(azienda);
            
        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("ragionesociale", azienda.getRagionesociale());
        templateModel.put("email", azienda.getEmail());
        templateModel.put("telefono", azienda.getTelefono());
        templateModel.put("indirizzo", azienda.getIndirizzo());
        templateModel.put("id", azienda.getEmail());
        templateModel.put("backend_address", backendAddress);

        emailService.sendHtmlMessage(staffEmail, "Richiesta account Badoni NetWork", templateModel, "account-request-template");

        return ResponseEntity.ok("{\"message\": \"Email inviata\"}");
    }

    @PostMapping("/register/utente")
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
        utenteService.saveUtente(utente);
        
        return ResponseEntity.ok("{\"message\": \"Account creato con successo\"}");
    }

    @GetMapping("/register/validate-otp")
    public void validateOTP(HttpServletResponse response) throws IOException {
        response.sendRedirect(frontendAddress+"/otp");
    }

    @PostMapping("/register/validate-otp")
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
            //! Implementare un meccanismo di blocco
            return ResponseEntity.badRequest().body("{\"message\": \"Tentativi esauriti\"}");
        }

        Optional<Azienda> azienda = aziendaService.findByCodice(requestBody.get("codice"));

        if (!azienda.isPresent()) {
            return ResponseEntity.badRequest().body("{\"message\": \"Codice invalido\"}");
        }

        Azienda aziendaget = azienda.get();
        aziendaget.setType(TipoAzienda.V);
        aziendaService.saveAzienda(aziendaget);

        return ResponseEntity.ok(azienda);
    }
    

    @PostMapping("/register/confirm-azienda")
    public ResponseEntity<String> confirmCompany(@RequestBody Azienda azienda, Errors errors) {

        Optional<Azienda> pending_azienda = aziendaService.findByEmail(azienda.getEmail());

        if (!pending_azienda.isPresent()) {
            return ResponseEntity.badRequest().body("{\"message\": \"L'azienda non è nelle richieste\"}");
        }

        Azienda new_azienda = pending_azienda.get();

        if (new_azienda.getType() != TipoAzienda.V) {
            return ResponseEntity.badRequest().body("{\"message\": \"L'azienda non è ancora stata verificata\"}");
        }

        azienda.setPassword(passwordEncoder.encode(azienda.getPassword()));
        azienda.setType(TipoAzienda.R);
        aziendaService.saveAzienda(azienda);

        return ResponseEntity.ok("{\"message\": \"Account creato\"}");
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpSession httpSession) {

        httpSession.removeAttribute("user-account");

        return ResponseEntity.ok().body("{\"message\": \"Logout eseguito\"}");
    }
}
