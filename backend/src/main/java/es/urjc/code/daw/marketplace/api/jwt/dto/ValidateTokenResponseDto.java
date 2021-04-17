package es.urjc.code.daw.marketplace.api.jwt.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ValidateTokenResponseDto {

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("token")
    private String token;

    @JsonProperty("is_valid")
    private boolean isValid;

}
