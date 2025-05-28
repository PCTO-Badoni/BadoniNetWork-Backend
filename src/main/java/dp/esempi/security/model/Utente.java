package dp.esempi.security.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
    private String emailpersonale;
    private String nome;
    private String cognome;
    private String password;
    @Enumerated(EnumType.STRING)
    private Pronome pronomi;
    private String telefono;
    private String indirizzo;
    private LocalDate datanascita;
    @Enumerated(EnumType.STRING)
    private Disponibile disponibile;
    private String curriculum;
    private LocalDateTime dataregistrazione;
    private LocalDateTime ultimoaccesso;
    private String note;
    private String ruolo;

    @ManyToOne
    @JoinColumn(name = "idarticolazione")
    private Articolazione articolazione;
}