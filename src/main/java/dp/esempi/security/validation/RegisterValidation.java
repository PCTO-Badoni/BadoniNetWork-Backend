package dp.esempi.security.validation;

import dp.esempi.security.model.Utente;
import dp.esempi.security.service.UtenteService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public record RegisterValidation(UtenteService utenteService) implements ConstraintValidator<emailEsistente, Utente>{

    @Override
    public void initialize(emailEsistente constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Utente u, ConstraintValidatorContext constraintValidatorContext) {
        if(utenteService.getByEmail(u.getEmail()) != null) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate("Email già esistente")
                    .addPropertyNode("email")
                    .addConstraintViolation();
            return false;
        }
        if(utenteService.getByUsername(u.getUsername()) != null) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate("Username già esistente")
                    .addPropertyNode("username")
                    .addConstraintViolation();
            return false;
        }
        return true;
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
