package dp.esempi.security.model;

import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="linguastudente")
public class LinguaStudente{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "idlingua")
    private Lingua lingua;
    @ManyToOne
    @JoinColumn(name = "username")
    private Utente studente;
    @ManyToOne
    @JoinColumn(name = "idlivello")
    private LivelloCompetenza livello;
}
