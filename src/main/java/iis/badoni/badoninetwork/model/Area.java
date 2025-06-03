package iis.badoni.badoninetwork.model;

import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="area")
public class Area{

    @Id
    private int idarea;
    private String descrizione;
}
