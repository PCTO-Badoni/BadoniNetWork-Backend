package dp.esempi.security.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
        public String index(){
            return "login";
        }

    @GetMapping("/admin/home")
    public String admin(){
        return "admin";
    }

    @GetMapping("/user/home")
    public String user(){
        return "user";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }
}

