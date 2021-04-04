package es.urjc.code.daw.marketplace.api.product.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlaceOrderResponseDto {


    public static PlaceOrderResponseDto successful() {
        return new PlaceOrderResponseDto();
    }

    @JsonProperty("success")
    private boolean success = true;

}
