package dp.esempi.security.validation;

import dp.esempi.security.model.Azienda;
import dp.esempi.security.model.TipoAzienda;
import dp.esempi.security.repository.AziendaRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AziendaRegisterValidation implements ConstraintValidator<AziendaValida, Azienda> {

    @Autowired
    private AziendaRepository aziendaRepository;

    @Override
    public void initialize(AziendaValida constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Azienda a, ConstraintValidatorContext constraintValidatorContext) {
        if (!a.getType().equals(TipoAzienda.W)) {
            return true;
        }

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

        String cellulare = "^(\\+39\\s?)?(\\d{2,4}\\s?\\d{6,8})$";
        String fisso = "^\\+\\d{1,3}\\s?\\d{1,14}$";

        if (!a.getTelefono().matches(cellulare) && !a.getTelefono().matches(fisso)) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate("Telefono invalido")
                    .addPropertyNode("telefono")
                    .addConstraintViolation();
            valido = false;
        }

        boolean valido2 = checkEmailAzienda(a.getEmail(), constraintValidatorContext);

        return valido && valido2;
    }

    private boolean checkEmailAzienda(String email, ConstraintValidatorContext constraintValidatorContext) {
        if (aziendaRepository.findByEmail(email).isPresent()) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate("Account esistente")
                    .addPropertyNode("errore")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
