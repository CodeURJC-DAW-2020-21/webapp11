package es.urjc.code.daw.marketplace.domain;

import es.urjc.code.daw.marketplace.util.TimeUtils;
import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "accumulative_discounts")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccumulativeDiscount {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Builder.Default
    private Boolean enabled = true;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "bulk_amount")
    private Integer bulkAmount;

    @Min(value = 1)
    @Max(value = 100)
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

    @PrePersist
    private void onCreate() {
        start = TimeUtils.now();
    }

    public void addConsumer(User user) {
        consumers.add(user);
    }

}
