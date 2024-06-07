package dp.esempi.security.model;

import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="livellocompetenze")
public class LivelloCompetenze {

    @Id
    private String idlivello;
    private String descrizione;
}
