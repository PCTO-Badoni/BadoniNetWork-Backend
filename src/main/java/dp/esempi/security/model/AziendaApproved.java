package dp.esempi.security.model;

import dp.esempi.security.validation.AziendaWaitingValida;
import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@Entity
@Table(name="aziende_approved")
@AziendaWaitingValida
public class AziendaApproved extends AziendaBase {

    private String codice;
}
