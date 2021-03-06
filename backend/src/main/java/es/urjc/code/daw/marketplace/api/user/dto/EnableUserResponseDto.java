package es.urjc.code.daw.marketplace.api.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EnableUserResponseDto {

    public static EnableUserResponseDto successful() {
        return new EnableUserResponseDto();
    }

    @JsonProperty("success")
    private boolean success = true;

}
