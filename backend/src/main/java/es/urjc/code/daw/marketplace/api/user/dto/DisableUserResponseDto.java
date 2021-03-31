package es.urjc.code.daw.marketplace.api.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DisableUserResponseDto {

    public static DisableUserResponseDto successful() {
        return new DisableUserResponseDto();
    }

    @JsonProperty("success")
    private boolean success = true;

}
