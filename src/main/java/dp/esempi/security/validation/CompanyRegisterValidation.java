package dp.esempi.security.validation;

import dp.esempi.security.model.Azienda;
import dp.esempi.security.model.Utente;
import dp.esempi.security.repository.AziendaRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Optional;
@SuppressWarnings("unused")
@Configuration
@Component
public class CompanyRegisterValidation implements ConstraintValidator<AziendaValida, Azienda>{

    private static final CompanyRegisterValidation holder= new CompanyRegisterValidation();
    private AziendaRepository aziendaRepository;

    @Bean(name = "company_validator")
    public static CompanyRegisterValidation bean(AziendaRepository aziendaRepository) {
        holder.aziendaRepository=aziendaRepository;
        return holder;
    }

    @Override
    public void initialize(AziendaValida constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Azienda a, ConstraintValidatorContext constraintValidatorContext) {
        boolean valido=true;
        Optional<Azienda> aziendaFind=holder.aziendaRepository.findByEmail(a.getEmail());
        if(!aziendaFind.isEmpty()) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate("Email già esistente")
                    .addPropertyNode("email")
                    .addConstraintViolation();
            valido=false;
        }

        aziendaFind=holder.aziendaRepository.findByragionesociale(a.getRagionesociale());
        if(!aziendaFind.isEmpty()) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate("Ragione Sociale già esistente")
                    .addPropertyNode("ragionesociale")
                    .addConstraintViolation();
            return false;
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
