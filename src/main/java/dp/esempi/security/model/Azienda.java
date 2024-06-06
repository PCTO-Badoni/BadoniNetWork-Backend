package dp.esempi.security.model;

import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="azienda")
//@AziendaWaitingValida
public class Azienda extends AziendaBase {

    private String password;
    private String citta;
    private String cap;
    private String cognomereferente;
    private String nomereferente;
    private String telreferente;
    private String emailreferente;
    private int idarea;
    private LocalDate ultimoaccesso;
    private String ruolo;

    @Override
    public String toString() {
        return "ClassName {" +
                "password='" + password + '\'' +
                ", citta='" + citta + '\'' +
                ", cap='" + cap + '\'' +
                ", cognomereferente='" + cognomereferente + '\'' +
                ", nomereferente='" + nomereferente + '\'' +
                ", telreferente='" + telreferente + '\'' +
                ", emailreferente='" + emailreferente + '\'' +
                ", idarea=" + idarea +
                ", ultimoaccesso=" + ultimoaccesso +
                ", ruolo='" + ruolo + '\'' +
                '}';
    }    
}
