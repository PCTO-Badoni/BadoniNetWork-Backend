package dp.esempi.security.controller;

import dp.esempi.security.model.Utente;
import dp.esempi.security.repository.UtenteRepository;
import dp.esempi.security.service.EmailService;
import jakarta.mail.MessagingException;
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
            System.out.println(errors.getAllErrors());
            System.out.println(utente.getUsername());
            System.out.println(utente.getEmail());
            System.out.println(utente.getPassword());
            return "register";
        } else {
            utente.setPassword(passwordEncoder.encode(utente.getPassword()));
            utente.setRole("USER");
            utenteRepository.save(utente);
            return "redirect:/login";
        }
    }

    @GetMapping("/request-sent")
    public String sentRequest() {
        return "requestsent";
    }

    @PostMapping("/send-email")
    public RedirectView sendEmail(@RequestParam String ragione_sociale, @RequestParam String email, @RequestParam String telefono, @RequestParam String indirizzo) throws MessagingException, IOException {

        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("ragione_sociale", ragione_sociale);
        templateModel.put("email", email);
        templateModel.put("telefono", telefono);
        templateModel.put("indirizzo", indirizzo);
        templateModel.put("id", email);

        emailService.sendHtmlMessage("cfrgnn06m28e507h@iisbadoni.edu.it", "Richiesta account Badoni NetWork", templateModel, "account-request-template");
        return new RedirectView("/register/request-sent");
    }
}
