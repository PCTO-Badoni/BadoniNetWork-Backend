package dp.esempi.security.controller;

import dp.esempi.security.model.Utente;
import dp.esempi.security.repository.UtenteRepository;
import dp.esempi.security.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/register")
@Controller
public class RegistrationController {
    @Autowired
    private UtenteRepository utenteRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private EmailService emailService;


    @GetMapping
    public String register(Model model) {
        model.addAttribute("utente", new Utente());
        return "register";
    }

    @PostMapping
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

    //Intercetta una richiesta alla pagina e la reindirizza alla pagina di register
    @GetMapping("/send-email")
    public String redirectSendMail() {
        return "redirect:/register";
    }

    @PostMapping("/send-email")
    public String sendEmail(HttpSession session, @RequestParam String ragione_sociale, @RequestParam String email, @RequestParam String telefono, @RequestParam String indirizzo) throws MessagingException, IOException {

        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("ragione_sociale", ragione_sociale);
        templateModel.put("email", email);
        templateModel.put("telefono", telefono);
        templateModel.put("indirizzo", indirizzo);
        templateModel.put("id", email);

        emailService.sendHtmlMessage("srmndr06p13e507g@iisbadoni.edu.it", "Richiesta account Badoni NetWork", templateModel, "account-request-template");
        
        session.setAttribute("requestSent", true);
        
        return "redirect:/register/request-sent";
    }

    @GetMapping("/request-sent")
    public String sentRequest(HttpSession session) {
        if (session.getAttribute("requestSent") == null) {
            return "redirect:/register";
        }

        session.removeAttribute("requestSent");
        return "requestsent";
    }
}
