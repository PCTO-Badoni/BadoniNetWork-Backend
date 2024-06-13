package dp.esempi.security.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="contatti")
public class Contatti{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idcontatto;
    private String emailstudente;
    private String emailazienda;
    @Enumerated(EnumType.STRING)
    private Tipo tipo;
    private LocalDateTime dataora;
    @Enumerated(EnumType.STRING)
    private Booleano visualizzato;
    private String messaggio;
}