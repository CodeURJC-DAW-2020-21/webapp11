package es.urjc.code.daw.marketplace.api.product.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FindProductResponseDto {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("category")
    private String category;

    @JsonProperty("price")
    private Integer price;

    @JsonProperty("hourly_price")
    private Double hourlyPrice;

    @JsonProperty("ram")
    private String ram;

    @JsonProperty("cores")
    private String cores;

    @JsonProperty("storage")
    private String storage;

    @JsonProperty("transfer")
    private String transfer;

}
