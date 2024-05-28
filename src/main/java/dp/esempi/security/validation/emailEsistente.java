package dp.esempi.security.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = RegisterValidation.class)
public @interface emailEsistente {
    String message() default "{Email-gia-esistente}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
