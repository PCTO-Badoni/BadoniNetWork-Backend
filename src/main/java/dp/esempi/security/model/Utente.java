package dp.esempi.security.model;

import dp.esempi.security.validation.UtenteValido;
import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="studente")
@UtenteValido
public class Utente {

    @Id
    private String email;
    private String nome;
    private String cognome;
    private String password;
    @Column(name="ruolo")
    private String role;

    @Override
    public String toString() {
        return "Utente{" +
                "nome='" + nome + '\'' +
                ", cognome='" + cognome + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
