package es.urjc.code.daw.marketplace.api.order.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RenewOrderResponseDto {

    public static RenewOrderResponseDto successful() {
        return new RenewOrderResponseDto();
    }

    @JsonProperty("success")
    private boolean success = true;

}
