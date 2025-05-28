package dp.esempi.security.controller;

import dp.esempi.security.model.Utente;
import dp.esempi.security.model.Azienda;
import dp.esempi.security.service.AziendaService;
import dp.esempi.security.service.UtenteService;
import jakarta.servlet.http.HttpSession;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RestController
public class LoginController {
    @Autowired
    private UtenteService utenteService;
    @Autowired
    private AziendaService aziendaService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    
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

    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpSession httpSession) {

        httpSession.removeAttribute("user-account");

        return ResponseEntity.ok().body("{\"message\": \"Logout eseguito\"}");
    }
}
