package es.urjc.code.daw.marketplace.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "one_time_discounts")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OneTimeDiscount {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long productId;

    @Size(min = 1, max = 100)
    private Integer discountPercentage;

    @Temporal(TemporalType.TIMESTAMP)
    private Date start;

    @Temporal(TemporalType.TIMESTAMP)
    private Date stop;

    @Singular
    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(
            name = "otd_user",
            joinColumns = { @JoinColumn(name = "otd_id", referencedColumnName = "id") },
            inverseJoinColumns = { @JoinColumn(name = "user_id", referencedColumnName = "id") }
    )
    private Set<User> consumers;

}
