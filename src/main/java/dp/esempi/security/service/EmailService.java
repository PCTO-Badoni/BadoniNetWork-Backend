package dp.esempi.security.service;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.IOException;
import java.util.Map;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private SpringTemplateEngine templateEngine;

    public void sendHtmlMessage(String to, String subject, Map<String, Object> templateModel, String mailtemplate) throws MessagingException, IOException {
        MimeMessage message = emailSender.createMimeMessage();

        // Imposta il destinatario
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));

        // Imposta l'oggetto
        message.setSubject(subject);

        Context context = new Context();
        context.setVariables(templateModel);

        String htmlContent = templateEngine.process(mailtemplate, context);

        Multipart mp = new MimeMultipart();
        MimeBodyPart htmlpart = new MimeBodyPart();
        htmlpart.setContent(htmlContent, "text/html");
        mp.addBodyPart(htmlpart);
        message.setContent(mp);

        emailSender.send(message);
    }
}