package iis.badoni.badoninetwork.model;

import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="articolazione")
public class Articolazione{

    @Id
    private String idarticolazione;
    private String descrizione;
}
