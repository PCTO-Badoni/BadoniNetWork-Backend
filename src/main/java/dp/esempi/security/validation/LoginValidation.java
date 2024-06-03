package dp.esempi.security.validation;

import dp.esempi.security.service.UtenteService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public record LoginValidation(UtenteService utenteService) implements ConstraintValidator<usernameNotFound, String>{

    @Override
    public void initialize(usernameNotFound constraintAnnotation){
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String u, ConstraintValidatorContext constraintValidatorContext) {
        if(utenteService.getByEmail(u) == null) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate("Username non valido")
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
