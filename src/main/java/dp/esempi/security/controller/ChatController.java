package dp.esempi.security.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    // Quando un messaggio viene inviato su /app/chat, lo invia a /topic/messages
    @MessageMapping("/message")
    @SendTo("/client/message")
    public String sendMessage(String message) {
        return message;
    }
}
