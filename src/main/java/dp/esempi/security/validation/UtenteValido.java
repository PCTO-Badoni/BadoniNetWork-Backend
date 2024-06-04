package dp.esempi.security.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UserRegisterValidation.class)

public @interface UtenteValido {
    String message() default "{Email-gia-esistente}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
