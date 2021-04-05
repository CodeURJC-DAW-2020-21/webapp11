package es.urjc.code.daw.marketplace.api.sale.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateSaleResponseDto {

    public static UpdateSaleResponseDto successful() {
        return new UpdateSaleResponseDto();
    }

    @JsonProperty("success")
    private boolean success = true;

}
