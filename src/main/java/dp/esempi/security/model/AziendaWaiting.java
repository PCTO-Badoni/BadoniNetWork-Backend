package dp.esempi.security.model;

import dp.esempi.security.validation.AziendaWaitingValida;
import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="aziende_waiting")
@AziendaWaitingValida
public class AziendaWaiting extends AziendaBase {

    private String codice;
}
