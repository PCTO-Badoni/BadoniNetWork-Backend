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

@RequestMapping("/register")
@Controller
public class RegistrationController {
    @Autowired
    private UtenteRepository utenteRepository;
    @Autowired
    private AziendaRepository aziendaRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private EmailService emailService;

    @GetMapping
    public String register(Model model) {
        model.addAttribute("utente", new Utente());
        model.addAttribute("company", new Azienda());
        return "register";
    }

    @PostMapping("/azienda")
    public String createCompany(HttpSession session, @ModelAttribute("company") Azienda azienda, Errors errors) throws MessagingException, IOException {
        if(errors.hasErrors()) {
            return "register";
        } else {
            azienda.setRole("USER");
            aziendaRepository.save(azienda);

            //Controlli

            //Se non ha mai fatto un registrazione => invia email
            sendEmail(session,azienda.getRagione_sociale(),azienda.getEmail(),azienda.getTelefono(),azienda.getIndirizzo());
            return "redirect:/register/request-sent";

            //passaggio di tabella


        }
    }

    @PostMapping("/utente")
    public String createUser(@ModelAttribute("utente") @Valid Utente utente, Errors errors) {
        if(errors.hasErrors()) {
            return "register";
        } else {
            utente.setPassword(passwordEncoder.encode(utente.getPassword()));
            utente.setRole("USER");
            utenteRepository.save(utente);
            return "redirect:/login";
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

    private void sendEmail(HttpSession session, String ragione_sociale, String email, String telefono,String indirizzo) throws MessagingException, IOException {

        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("ragione_sociale", ragione_sociale);
        templateModel.put("email", email);
        templateModel.put("telefono", telefono);
        templateModel.put("indirizzo", indirizzo);
        templateModel.put("id", email);

        session.setAttribute("requestSent", true);

        emailService.sendHtmlMessage("srmndr06p13e507g@iisbadoni.edu.it", "Richiesta account Badoni NetWork", templateModel, "account-request-template");
    }
}