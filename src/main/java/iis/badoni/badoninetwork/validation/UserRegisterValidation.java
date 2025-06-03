package iis.badoni.badoninetwork.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import iis.badoni.badoninetwork.model.Booleano;
import iis.badoni.badoninetwork.model.Utente;
import iis.badoni.badoninetwork.model.VerificaEmailStudente;
import iis.badoni.badoninetwork.repository.UtenteRepository;
import iis.badoni.badoninetwork.repository.VerificaEmailStudenteRepository;

import java.util.Optional;
@Configuration
@Component
public class UserRegisterValidation implements ConstraintValidator<UtenteValido, Utente>{

    private static final UserRegisterValidation holder=new UserRegisterValidation();
    private UtenteRepository utenteRepository;

    @Autowired
    private VerificaEmailStudenteRepository verificaEmailStudentiRepository;

    @Bean(name = "user_validator")
    public static UserRegisterValidation bean(UtenteRepository utenteRepository) {
        holder.utenteRepository=utenteRepository;
        return holder;
    }

    @Override
    public void initialize(UtenteValido constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Utente u, ConstraintValidatorContext constraintValidatorContext) {
        boolean valido = true;

        if (!u.getEmail().endsWith("iisbadoni.edu.it")) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate("Email invalida")
                    .addPropertyNode("errore")
                    .addConstraintViolation();
            valido = false;
        }

        Optional<Utente> utenteFind=holder.utenteRepository.findByEmail(u.getEmail());
        if(!utenteFind.isEmpty()) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate("Email già esistente")
                    .addPropertyNode("email")
                    .addConstraintViolation();
            valido = false;
        }

        String regex = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[a-z])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).{8,}$";

        if (!u.getPassword().matches(regex)) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate("Password insicura")
                    .addPropertyNode("password")
                    .addConstraintViolation();
            valido = false;
        }

        if (u.getNome().strip().isEmpty()) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate("Nome invalido")
                    .addPropertyNode("nome")
                    .addConstraintViolation();
            valido = false;
        }

        if (u.getCognome().strip().isEmpty()) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate("Cognome invalido")
                    .addPropertyNode("cognome")
                    .addConstraintViolation();
            valido = false;
        }

        VerificaEmailStudente verifica = verificaEmailStudentiRepository.findByEmail(u.getEmail()).orElse(null);

        if (verifica == null) {
            valido = false;
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate("Email inesistente nella validazione")
                    .addPropertyNode("verifica")
                    .addConstraintViolation();
        } else {
            if (verifica.getVerificato().equals(Booleano.N)) {
                valido = false;
                constraintValidatorContext.disableDefaultConstraintViolation();
                constraintValidatorContext.buildConstraintViolationWithTemplate("Email non verificata")
                        .addPropertyNode("verifica")
                        .addConstraintViolation();
            } else {
                verificaEmailStudentiRepository.delete(verifica);
            }
        }

        return valido;
    }

    @Override
    public boolean equals(Object obj) {
        return false;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public String toString() {
        return "";
    }
}
