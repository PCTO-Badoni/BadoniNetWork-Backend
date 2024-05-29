package dp.esempi.security.controller;

import dp.esempi.security.model.Utente;
import dp.esempi.security.repository.UtenteRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequestMapping("/register")
@Controller
public class RegistrationController {
    @Autowired
    private UtenteRepository utenteRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;



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
            utenteRepository.save(utente);
            return "redirect:/login";
        }
    }
}
