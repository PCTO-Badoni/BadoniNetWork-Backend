package dp.esempi.security.controller;

import dp.esempi.security.model.Utente;
import dp.esempi.security.service.UtenteService;
import jakarta.validation.Valid;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.GetMapping;

@RequestMapping("/login")
@CrossOrigin
@RestController
public class LoginController {
    @Autowired
    private UtenteService utenteService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping()
    public String getMethodName() {
        return "richiesta get";
    }
    

    @PostMapping
    public Optional<Utente> findUser(@Valid @RequestBody Utente utente, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        utente.setPassword(passwordEncoder.encode(utente.getPassword()));
        Optional<Utente> user = utenteService.findByEmailAndPassword(utente.getEmail(), utente.getPassword());
        if (bindingResult.hasErrors()) {
            String error = bindingResult.getFieldError().getDefaultMessage();
            redirectAttributes.addFlashAttribute("error", error);
        }
        return user;
    }
}
