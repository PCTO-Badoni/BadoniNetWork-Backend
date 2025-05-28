package dp.esempi.security.model;

import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="lingua")
public class Lingua {

    @Id
    private int idlingua;
    private String descrizione;
}
