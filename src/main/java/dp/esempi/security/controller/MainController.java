package dp.esempi.security.controller;

import dp.esempi.security.service.EmailService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Controller
public class MainController {
    @Autowired
    private EmailService emailService;

    Random random = new Random();

    @GetMapping("/")
    public String index(){
        return "index";
    }

    @GetMapping("/admin/home")
    public String admin(){
        return "admin";
    }

    @GetMapping("/user/home")
    public String user(){
        return "user";
    }

    @GetMapping("/admin/accept-request/{email}")
    public String acceptRequest(@PathVariable String email) throws MessagingException, IOException {

        int randomNumber = random.nextInt(1000000);
        String codice = ""+randomNumber;

        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("codice", codice);
        emailService.sendHtmlMessage(email, "Accettazione account", templateModel, "request-response-template");
        return "message";
    }

    @GetMapping("/admin/deny-request/{email}")
    public String denyRequest(@PathVariable String email) throws MessagingException, IOException {
        Map<String, Object> templateModel = new HashMap<>();
        emailService.sendHtmlMessage("srmndr06p13e507g@iisbadoni.edu.it", "Risposta account Badoni NetWork", templateModel, "account-request-template");
        return "message";
    }

    @GetMapping("/register/request-sent")
    public String sentRequest() {
        return "requestsent";
    }

    @PostMapping("/register/send-email")
    public RedirectView sendEmail(@RequestParam String ragione_sociale, @RequestParam String email, @RequestParam String telefono, @RequestParam String indirizzo) throws MessagingException, IOException {

        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("ragione_sociale", ragione_sociale);
        templateModel.put("email", email);
        templateModel.put("telefono", telefono);
        templateModel.put("indirizzo", indirizzo);
        templateModel.put("id", email);

        emailService.sendHtmlMessage("srmndr06p13e507g@iisbadoni.edu.it", "Richiesta account Badoni NetWork", templateModel, "account-request-template");
        return new RedirectView("/register/request-sent");
    }
}

