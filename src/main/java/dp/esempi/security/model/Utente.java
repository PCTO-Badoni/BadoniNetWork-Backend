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
    private String nome;
    private String cognome;
    private String password;
    @Enumerated(EnumType.STRING)
    private Pronome pronomi;
    private String telefono;
    private String indirizzo;
    private String cap;
    private String citta;
    private LocalDate datanascita;
    @Enumerated(EnumType.STRING)
    private Disponibile disponibile;
    private String curriculum;
    private LocalDateTime dataregistrazione;
    private LocalDate ultimoaccesso;
    private String idarticolazione;
    private String note;
    private String ruolo;
}