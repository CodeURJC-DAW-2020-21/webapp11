package es.urjc.code.daw.marketplace.api.order.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CancelOrderResponseDto {

    public static CancelOrderResponseDto successful() {
        return new CancelOrderResponseDto();
    }

    @JsonProperty("success")
    private boolean success = true;

}
