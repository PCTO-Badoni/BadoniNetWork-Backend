package dp.esempi.security.controller;

import dp.esempi.security.model.Utente;
import dp.esempi.security.service.UtenteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequestMapping("/login")
@Controller
public class LoginController {
    @Autowired
    private UtenteService utenteService;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @GetMapping
    public String login(){
        return "login";
    }

    @PostMapping
    public void findUser(@Valid @ModelAttribute Utente utente, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            String error = bindingResult.getFieldError().getDefaultMessage();
            redirectAttributes.addFlashAttribute("error", error);
        }
    }
}