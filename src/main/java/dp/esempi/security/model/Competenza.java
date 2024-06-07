package dp.esempi.security.model;

import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="competenze")
public class Competenza{

    @Id
    private int idcompetenza;
    private String descrizione;
}
