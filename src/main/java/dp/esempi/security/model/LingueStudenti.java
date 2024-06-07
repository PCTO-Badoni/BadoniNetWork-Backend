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
    private int id;
    private int idlingua;
    private String username;
    private String idlivello;
}
