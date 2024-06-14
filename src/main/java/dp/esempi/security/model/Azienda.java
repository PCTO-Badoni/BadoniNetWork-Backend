package dp.esempi.security.model;

import java.time.LocalDate;

import dp.esempi.security.validation.AziendaValida;
import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="azienda")
@AziendaValida
public class Azienda {

    @Id
    private String email;
    private String ragionesociale;
    private String telefono;
    private String indirizzo;
    private String password;
    private String cognomereferente;
    private String nomereferente;
    private String telreferente;
    private String emailreferente;
    private int idarea;
    private LocalDate ultimoaccesso;
    private String ruolo;
    private String codice;
    @Enumerated(EnumType.STRING)
    private TipoAzienda type;
}
