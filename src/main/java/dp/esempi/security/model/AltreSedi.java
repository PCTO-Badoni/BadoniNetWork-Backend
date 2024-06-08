package dp.esempi.security.model;

import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="altresedi")
public class AltreSedi{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idsedi;
    private String email;
    private String indirizzo;
    private String cap;
    private String citta;
}
