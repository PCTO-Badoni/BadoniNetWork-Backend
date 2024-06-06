package dp.esempi.security.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public abstract class AziendaBase {

    @Id
    private String email;
    private String ragionesociale;
    private String telefono;
    private String indirizzo;
}