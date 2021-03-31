package es.urjc.code.daw.marketplace.api.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterUserResponseDto {

    public static RegisterUserResponseDto successful() {
        return new RegisterUserResponseDto();
    }

    @JsonProperty("success")
    private boolean success = true;

}
