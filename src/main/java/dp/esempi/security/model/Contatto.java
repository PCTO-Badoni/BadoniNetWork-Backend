package dp.esempi.security.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="contatto")
public class Contatto{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idcontatto;
    @Enumerated(EnumType.STRING)
    private TipoContatto tipo;
    private LocalDateTime dataora;
    @Enumerated(EnumType.STRING)
    private Booleano visualizzato;
    private String messaggio;

    @ManyToOne
    @JoinColumn(name = "emailstudente")
    private Utente studente;
    @ManyToOne
    @JoinColumn(name = "emailazienda")
    private Azienda azienda;
}