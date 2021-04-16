package es.urjc.code.daw.marketplace.api.order.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlaceOrderRequestDto {

    @JsonProperty("product_id")
    private Long productId;

}
