package es.urjc.code.daw.marketplace.api.order.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Date;

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
