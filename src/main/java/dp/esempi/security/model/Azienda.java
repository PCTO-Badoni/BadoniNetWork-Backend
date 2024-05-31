package dp.esempi.security.model;

import dp.esempi.security.validation.AziendaValida;
import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="aziende_waiting")
@AziendaValida
public class Azienda {

    @Id

    private String ragionesociale;
    private String email;
    private String telefono;
    private String indirizzo;
    @Column(name="ruolo")
    private String role;

    @Override
    public String toString() {
        return "Utente{" +
                "ragione sociale='" + ragionesociale + '\'' +
                ", email='" + email + '\'' +
                ", telefono='" + telefono + '\'' +
                ", indirizzo='" + indirizzo + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
