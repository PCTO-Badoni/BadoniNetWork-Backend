package dp.esempi.security.model;

import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="lingue")
public class Lingua {

    @Id
    private int idlingua;
    private String descrizione;
}
