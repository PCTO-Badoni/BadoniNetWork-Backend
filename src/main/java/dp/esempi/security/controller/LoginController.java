package dp.esempi.security.controller;

import dp.esempi.security.model.Utente;
import dp.esempi.security.service.UtenteService;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequestMapping("/login")
@CrossOrigin(origins = {"http://localhost:3001", "http://127.0.0.1:3001"})
@RestController
public class LoginController {
    @Autowired
    private UtenteService utenteService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @PostMapping("/authenticate")
    public ResponseEntity<?> findUser(@RequestBody Utente utente, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        Optional<Utente> user = utenteService.findByEmailAndPassword(utente.getEmail(), utente.getPassword(), passwordEncoder);
        
        if (user.equals(Optional.empty())) {
            // String error = bindingResult.getFieldError().getDefaultMessage();
            // redirectAttributes.addFlashAttribute("error", error);
            return ResponseEntity.badRequest().body("{\"message\": \"Credenziali errate\"}");
        }

        return ResponseEntity.ok().body(user.get());
    }
}
