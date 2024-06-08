package dp.esempi.security.model;

import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="competenzestudenti")
public class CompetenzeStudenti{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String email;
    private int idcompetenza;
    private String idlivello;
}
