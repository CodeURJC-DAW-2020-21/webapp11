package es.urjc.code.daw.marketplace.domain;

import lombok.*;
import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

@ToString
@Entity
@Table(name = "ordered_services")
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
        if(creationDate == null) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.clear(Calendar.MINUTE);
            calendar.clear(Calendar.SECOND);
            calendar.clear(Calendar.MILLISECOND);
            creationDate = calendar.getTime();
        }
        if(expiryDate == null) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, 1);
            expiryDate = calendar.getTime();
        }
    }

    public boolean isExpired() {
        Date today = new Date();
        return today.after(expiryDate);
    }

    public void applyDiscount(int discountPercentage) {
        int finalCost = ((100 - discountPercentage) * product.getPrice()) / 100;
        this.setFinalCost(finalCost);
    }

}
