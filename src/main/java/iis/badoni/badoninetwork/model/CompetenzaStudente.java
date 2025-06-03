package iis.badoni.badoninetwork.model;

import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="competenzastudente")
public class CompetenzaStudente{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "email")
    private Utente studente;
    @ManyToOne
    @JoinColumn(name = "idcompetenza")
    private Competenza competenza;
    @ManyToOne
    @JoinColumn(name = "idlivello")
    private LivelloCompetenza livelloCompetenza;
}
