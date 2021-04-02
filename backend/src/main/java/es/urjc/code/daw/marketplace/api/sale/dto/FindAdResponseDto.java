package es.urjc.code.daw.marketplace.api.sale.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.annotation.PostConstruct;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FindAdResponseDto {

    @JsonProperty("discount")
    private Integer discount;

    @JsonProperty("price")
    private Integer price;

    @JsonProperty("discounted_price")
    private Integer discountedPrice;

    @JsonProperty("bulk_amount")
    private Integer bulkAmount;

    @JsonProperty("ram")
    private String ram;

    @JsonProperty("cores")
    private String cores;

    @JsonProperty("storage")
    private String storage;

    @JsonProperty("transfer")
    private String transfer;

    @PostConstruct
    public void setDiscountedCost() {
        this.discountedPrice = ((100 - discount) * price) / 100;
    }

}
