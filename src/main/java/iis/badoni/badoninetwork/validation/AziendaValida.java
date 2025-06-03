package iis.badoni.badoninetwork.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AziendaRegisterValidation.class)

public @interface AziendaValida {
    String message() default "{Email-gia-esistente}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
