package dp.esempi.security.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AziendaWaitingRegisterValidation.class)

public @interface AziendaWaitingValida {
    String message() default "{Email-gia-esistente}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
