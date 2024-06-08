package dp.esempi.security.model;

import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="linguestudenti")
public class LingueStudenti{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int idlingua;
    private String username;
    private String idlivello;
}
