package iis.badoni.badoninetwork.model;

import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="altrasede")
public class AltraSede{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idsede;
    private String indirizzo;
    private String cap;
    private String citta;

    @ManyToOne
    @JoinColumn(name = "email")
    private Azienda azienda;
}
