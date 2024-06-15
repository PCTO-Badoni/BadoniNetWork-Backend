package dp.esempi.security.controller;

import dp.esempi.security.model.Utente;
import dp.esempi.security.model.Azienda;
import dp.esempi.security.service.AziendaService;
import dp.esempi.security.service.UtenteService;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequestMapping("/login")
@RestController
public class LoginController {
    @Autowired
    private UtenteService utenteService;
    @Autowired
    private AziendaService aziendaService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @PostMapping()
    public ResponseEntity<?> findUser(@RequestBody Utente utente, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        
        Optional<Utente> user = utenteService.findByEmailAndPassword(utente.getEmail(), utente.getPassword(), passwordEncoder);
        // Optional<Azienda> company = aziendaService.findByEmailAndPassword(azienda.getEmail(), azienda.getPassword(), passwordEncoder);
        
        // if (user.isEmpty() && company.isEmpty()) {
        //     return ResponseEntity.badRequest().body("{\"message\": \"Credenziali errate\"}");
        // }

        return ResponseEntity.ok().body(user.get());
    }
}
