package dp.esempi.security.validation;

import dp.esempi.security.model.Azienda;
import dp.esempi.security.repository.AziendaRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CompanyRegisterValidation implements ConstraintValidator<AziendaValida, Azienda> {

    @Autowired
    private AziendaRepository aziendaRepository;

    @Override
    public void initialize(AziendaValida constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Azienda a, ConstraintValidatorContext constraintValidatorContext) {
        boolean valido = true;
        Optional<Azienda> aziendaFind = aziendaRepository.findByEmail(a.getEmail());
        if (aziendaFind.isPresent()) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate("Email già esistente")
                    .addPropertyNode("email")
                    .addConstraintViolation();
            valido = false;
        }

        if (!valido) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate("Richiesta già inviata")
                    .addPropertyNode("errore")
                    .addConstraintViolation();
        }

        boolean valido2 = checkEmailAzienda(a.getEmail(), constraintValidatorContext);
        boolean valido3 = checkEmailApproved(a.getEmail(), constraintValidatorContext);

        return valido && valido2 && valido3;
    }

    private boolean checkEmailAzienda(String email, ConstraintValidatorContext constraintValidatorContext) {
        if (aziendaRepository.countByEmailInAzienda(email) > 0) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate("Account esistente")
                    .addPropertyNode("errore")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }

    private boolean checkEmailApproved(String email, ConstraintValidatorContext constraintValidatorContext) {
        if (aziendaRepository.countByEmailInAziendeApproved(email) > 0) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate("Account già approvato")
                    .addPropertyNode("errore")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
