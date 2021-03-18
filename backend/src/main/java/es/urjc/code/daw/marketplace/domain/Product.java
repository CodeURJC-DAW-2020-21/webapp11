package es.urjc.code.daw.marketplace.domain;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@ToString
@Entity
@Table(name = "products")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String category;

    private Integer price;

    private String ram;

    private String cores;

    private String storage;

    private String transfer;

    @OneToMany(mappedBy = "product")
    @Builder.Default
    private Set<Order> orders = new HashSet<>();

    public int getMonthlyPrice() {
        return getPrice();
    }

    @SuppressWarnings("unused")
    public double getHourlyPrice() {
        return ((double) getMonthlyPrice() / 24d) / (5d / 0.18d);
    }

}
