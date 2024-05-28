package dp.esempi.security.model;

import dp.esempi.security.validation.emailEsistente;
import dp.esempi.security.validation.usernameNotFound;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="utenti")
@emailEsistente
public class Utente {

    @Id
    @usernameNotFound
    private String username;
    private String email;
    private String password;
    @Column(name="ruolo")
    private String role;

    @Override
    public String toString() {
        return "Utente{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
