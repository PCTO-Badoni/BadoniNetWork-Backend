package iis.badoni.badoninetwork.model;

import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="competenza")
public class Competenza{

    @Id
    private int idcompetenza;
    private String descrizione;
}
