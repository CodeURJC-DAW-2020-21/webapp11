package es.urjc.code.daw.marketplace.api.sale.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DisableSaleResponseDto {

    public static DisableSaleResponseDto successful() {
        return new DisableSaleResponseDto();
    }

    @JsonProperty("success")
    private boolean success = true;

}
