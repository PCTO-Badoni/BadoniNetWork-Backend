package dp.esempi.security.controller;

import dp.esempi.security.model.Utente;
import dp.esempi.security.service.UtenteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@CrossOrigin
@RequestMapping("/login")
@RestController
public class LoginController {
    
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