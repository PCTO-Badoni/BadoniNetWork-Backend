package dp.esempi.security.controller;

import dp.esempi.security.model.Azienda;
import dp.esempi.security.model.Utente;
import dp.esempi.security.repository.AziendaRepository;
import dp.esempi.security.repository.UtenteRepository;
import dp.esempi.security.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
    public String createCompany(HttpSession session, @Valid Azienda azienda, Errors errors) throws MessagingException, IOException {
        if(errors.hasErrors()) {
            return "register";
        } else {
            // Set the primary key of the azienda entity
            azienda.setRole("USER");
            aziendaRepository.save(azienda);

            //Controlli

            //! 1) verificare la ragione sociale e l'email se esistono all'interno del database sia waiting che approved

            //? 2) SE NON ESISTE invio la mail alla segreteria per l'approvazione
            //? 2) SE ESITE 
                // 2.2 SE ESISTE NEL WAITING mostro un messaggio per dire che la richiesta è già stata fatta
                // 2.2 SE ESISTE NEL APPROVED dire che l'account è già presente e reindirizzarlo alla login
                
            //! 3) NELLA LOGIN

            //? 4) SE HA UNA PASSWORD SETTATA lo faccio accedere normalmente
            //? 4) SE NON HA UN PASSWORD SETTATA lo porto alla pagina per inserire il codice e proseguire con l'inserimento dati

            //Se non ha mai fatto un registrazione => invia email
            sendEmail(session,azienda.getRagionesociale(),azienda.getEmail(),azienda.getTelefono(),azienda.getIndirizzo());
            return "redirect:/register/request-sent";

            //passaggio di tabella
        }
    }

    @PostMapping("/utente")
    public String createUser(@Valid Utente utente, Errors errors) {
        if(errors.hasErrors()) {
            return "register";
        } else {
            utente.setPassword(passwordEncoder.encode(utente.getPassword()));
            utente.setRole("USER");
            utenteRepository.save(utente);
            return null;
        }
    }

    @GetMapping("/request-sent")
    public String sentRequest(HttpSession session) {
        if(session.getAttribute("requestSent") == null) {
            return "redirect:/register";
        }

        session.removeAttribute("requestSent");
        return "requestsent";
    }

    private void sendEmail(HttpSession session, String ragionesociale, String email, String telefono,String indirizzo) throws MessagingException, IOException {

        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("ragionesociale", ragionesociale);
        templateModel.put("email", email);
        templateModel.put("telefono", telefono);
        templateModel.put("indirizzo", indirizzo);
        templateModel.put("id", email);

        session.setAttribute("requestSent", true);

        emailService.sendHtmlMessage("srmndr06p13e507g@iisbadoni.edu.it", "Richiesta account Badoni NetWork", templateModel, "account-request-template");
    }
}