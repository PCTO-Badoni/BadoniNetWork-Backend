package dp.esempi.security.validation;

import dp.esempi.security.model.Utente;
import dp.esempi.security.repository.UtenteRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Optional;
@SuppressWarnings("unused")
@Configuration
@Component
public class RegisterValidation implements ConstraintValidator<UtenteValido, Utente>{

    private static final RegisterValidation holder=new RegisterValidation();
    private UtenteRepository utenteRepository;

    @Bean(name = "user_validator")
    public static RegisterValidation bean(UtenteRepository utenteRepository) {
        holder.utenteRepository=utenteRepository;
        return holder;
    }

    @Override
    public void initialize(UtenteValido constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Utente u, ConstraintValidatorContext constraintValidatorContext) {
        boolean valido=true;
        Optional<Utente> utenteFind=holder.utenteRepository.findByEmail(u.getEmail());
        if(!utenteFind.isEmpty()) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate("Email già esistente")
                    .addPropertyNode("email")
                    .addConstraintViolation();
            valido=false;
        }
        utenteFind=holder.utenteRepository.findByUsername(u.getEmail());
        if(!utenteFind.isEmpty()) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate("Username già esistente")
                    .addPropertyNode("username")
                    .addConstraintViolation();
            return false;
        }

        System.out.println("Utente valido");
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
