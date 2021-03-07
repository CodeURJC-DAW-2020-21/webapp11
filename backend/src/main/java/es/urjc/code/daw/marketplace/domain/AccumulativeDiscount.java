package es.urjc.code.daw.marketplace.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "accumulative_discount")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccumulativeDiscount {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "bulk_amount")
    private Integer bulkAmount;

    @Size(min = 1, max = 100)
    @Column(name = "discount_percentage")
    private Integer discountPercentage;

    @Temporal(TemporalType.TIMESTAMP)
    private Date start;

    @Temporal(TemporalType.TIMESTAMP)
    private Date stop;

    @Singular
    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(
            name = "ad_user",
            joinColumns = { @JoinColumn(name = "ad_id", referencedColumnName = "id") },
            inverseJoinColumns = { @JoinColumn(name = "user_id", referencedColumnName = "id") }
    )
    private Set<User> consumers;

}
