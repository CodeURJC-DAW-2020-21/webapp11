package es.urjc.code.daw.marketplace.domain;

import lombok.*;

import javax.persistence.*;

@ToString
@Entity
@Table(name = "product")
@Data
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

}
