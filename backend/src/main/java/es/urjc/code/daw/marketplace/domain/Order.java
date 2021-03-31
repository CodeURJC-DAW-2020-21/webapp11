package es.urjc.code.daw.marketplace.domain;

import es.urjc.code.daw.marketplace.util.TimeUtils;
import lombok.*;
import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

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

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "creation_date")
    private Date creationDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "expiry_date")
    private Date expiryDate;

    @PrePersist
    private void onCreate() {
        if(creationDate == null) {
            creationDate = TimeUtils.now();
        }
        if(expiryDate == null) {
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Europe/Madrid"));
            calendar.add(Calendar.MONTH, 1);
            expiryDate = calendar.getTime();
        }
    }

    public boolean isExpired() {
        Date today = TimeUtils.now();
        return today.after(expiryDate);
    }

    public void applyDiscount(int discountPercentage) {
        int finalCost = ((100 - discountPercentage) * product.getPrice()) / 100;
        this.setFinalCost(finalCost);
    }

}
