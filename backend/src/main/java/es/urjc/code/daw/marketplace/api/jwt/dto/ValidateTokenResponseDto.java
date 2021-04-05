package es.urjc.code.daw.marketplace.api.jwt.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ValidateTokenResponseDto {

    public static ValidateTokenResponseDto create(Long userId, String token, boolean isValid) {
        return new ValidateTokenResponseDto(userId, token, isValid);
    }

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("token")
    private String token;

    @JsonProperty("is_valid")
    private boolean isValid;

}
