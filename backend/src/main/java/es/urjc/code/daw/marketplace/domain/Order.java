package es.urjc.code.daw.marketplace.domain;

import lombok.*;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

@ToString
@Entity
@Table(name = "purchased_orders")
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

    @Temporal(TemporalType.DATE)
    private Date expiryDate;

    @PrePersist
    private void onCreate() {
        creationDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 1);
        expiryDate = calendar.getTime();
    }

    public boolean isExpired() {
        Date today = new Date();
        return today.after(expiryDate);
    }

}
