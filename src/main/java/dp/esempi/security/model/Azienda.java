package dp.esempi.security.model;

import dp.esempi.security.validation.UtenteValido;
import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="aziende_waiting")
public class Azienda {

    @Id

    private String ragione_sociale;
    private String email;
    private String telefono;
    private String indirizzo;
    @Column(name="ruolo")
    private String role;

    @Override
    public String toString() {
        return "Utente{" +
                "ragione sociale='" + ragione_sociale + '\'' +
                ", email='" + email + '\'' +
                ", telefono='" + telefono + '\'' +
                ", indirizzo='" + indirizzo + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
