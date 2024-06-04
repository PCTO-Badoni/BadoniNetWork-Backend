package dp.esempi.security.validation;

import dp.esempi.security.model.Utente;
import dp.esempi.security.repository.UtenteRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Optional;
@Configuration
@Component
public class UserRegisterValidation implements ConstraintValidator<UtenteValido, Utente>{

    private static final UserRegisterValidation holder=new UserRegisterValidation();
    private UtenteRepository utenteRepository;

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
