package es.urjc.code.daw.marketplace.api.sale.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.annotation.PostConstruct;
import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FindAdResponseDto {

    @JsonProperty("price")
    private Integer price;

    @JsonProperty("discount")
    private Integer discount;

    @JsonProperty("discount_price")
    private Integer discountedPrice;

    @JsonProperty("bulk_amount")
    private Integer bulkAmount;

    @JsonProperty("product_id")
    private Long discountedProductId;

    @JsonProperty("start_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date startDate;

    @JsonProperty("expiry_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date expiryDate;

    @JsonProperty("ram")
    private String ram;

    @JsonProperty("cores")
    private String cores;

    @JsonProperty("storage")
    private String storage;

    @JsonProperty("transfer")
    private String transfer;

    public void setPrice(Integer price) {
        this.price = price;
        updateDiscountedCost();
    }

    @PostConstruct
    public void updateDiscountedCost() {
        this.discountedPrice = ((100 - discount) * price) / 100;
    }

}
