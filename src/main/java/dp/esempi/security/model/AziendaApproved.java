package dp.esempi.security.model;

import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@Entity
@Table(name="aziende_approved")
public class AziendaApproved extends AziendaPending {
}
