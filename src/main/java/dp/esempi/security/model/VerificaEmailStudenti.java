package dp.esempi.security.model;

import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="verificaemail_studenti")
public class VerificaEmailStudenti {

    @Id
    private String email;
    private String codice;
    @Enumerated(EnumType.STRING)
    private Booleano verificato;
}
