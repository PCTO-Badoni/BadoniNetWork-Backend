package dp.esempi.security.controller;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import dp.esempi.security.model.Utente;
import dp.esempi.security.repository.UtenteRepository;


@RequestMapping("/api")
@CrossOrigin (origins = {"http://localhost:3001", "http://127.0.0.1:3001"})
@RestController
public class ApiController {

    @Autowired
    UtenteRepository utenteRepository;

    @PostMapping("/verify-email")
    public ResponseEntity<String> postMethodName(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        
        if (!email.endsWith("iisbadoni.edu.it")) {
            return ResponseEntity.badRequest().body("{\"message\": \"Email invalida\"}");
        }

        Optional<Utente> utenteFind= utenteRepository.findByEmail(email);
        if(!utenteFind.isEmpty()) {
            return ResponseEntity.badRequest().body("{\"message\": \"Email già esistente\"}");
        }

        
        return ResponseEntity.ok().body("{\"message\": \"Email valida\"}");
    }
    
}