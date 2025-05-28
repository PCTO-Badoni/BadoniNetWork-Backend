package dp.esempi.security.model;

import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="livellocompetenza")
public class LivelloCompetenza {

    @Id
    private String idlivello;
    private String descrizione;
}
