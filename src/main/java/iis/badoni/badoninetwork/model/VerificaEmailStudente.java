package iis.badoni.badoninetwork.model;

import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="verificaemail_studente")
public class VerificaEmailStudente {

    @Id
    private String email;
    private String codice;
    @Enumerated(EnumType.STRING)
    private Booleano verificato;
}
