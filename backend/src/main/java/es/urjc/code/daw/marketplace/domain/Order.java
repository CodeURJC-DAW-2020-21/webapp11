package es.urjc.code.daw.marketplace.domain;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

import static javax.persistence.FetchType.EAGER;

@ToString
@Entity
@Table(name = "orders")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "final_cost")
    private Integer finalCost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_order")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_order")
    private User user;

    @Temporal(TemporalType.DATE)
    private Date creationDate;

    @PrePersist
    private void onCreate() {
        creationDate = new Date();
    }

}
