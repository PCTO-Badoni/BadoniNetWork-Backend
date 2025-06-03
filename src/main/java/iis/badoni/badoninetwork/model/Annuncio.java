package iis.badoni.badoninetwork.model;

import iis.badoni.badoninetwork.validation.AziendaValida;
import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="annuncio")
@AziendaValida
public class Annuncio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String ruolo;
    @Enumerated(EnumType.STRING)
    private TipoContratto contratto;
    @Enumerated(EnumType.STRING)
    private ModalitaContratto modalita;
    private float retribuzione;
    private String descrizione;

    @ManyToOne
    @JoinColumn(name = "email_azienda")
    private Azienda azienda;
}
