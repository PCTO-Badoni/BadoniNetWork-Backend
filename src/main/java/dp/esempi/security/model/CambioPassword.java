package dp.esempi.security.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="cambiopassword")

public class CambioPassword {

    @Id
    int id;
    String email;
    String codice;
}
