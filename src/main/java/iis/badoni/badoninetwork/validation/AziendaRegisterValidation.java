package iis.badoni.badoninetwork.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import iis.badoni.badoninetwork.model.Azienda;
import iis.badoni.badoninetwork.model.TipoAzienda;
import iis.badoni.badoninetwork.repository.AziendaRepository;

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
        if(a.getType() != null) {
            if (!a.getType().equals(TipoAzienda.W)) {
                return true;
            }
        }

        boolean valido = true;
        Optional<Azienda> aziendaFind = aziendaRepository.findByEmail(a.getEmail());
        if (aziendaFind.isPresent()) {
            if (aziendaFind.get().getType().equals(TipoAzienda.W)) {
                constraintValidatorContext.disableDefaultConstraintViolation();
                constraintValidatorContext.buildConstraintViolationWithTemplate("Richiesta già inviata")
                        .addPropertyNode("errore")
                        .addConstraintViolation();
                valido = false;
            }

            if (aziendaFind.get().getType().equals(TipoAzienda.R)) {
                constraintValidatorContext.disableDefaultConstraintViolation();
                constraintValidatorContext.buildConstraintViolationWithTemplate("Account esistente")
                        .addPropertyNode("errore")
                        .addConstraintViolation();
                valido = false;
            }
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

        return valido;
    }
}
