package es.urjc.code.daw.marketplace.api.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserResponseDto {

    public static UpdateUserResponseDto successful() {
        return new UpdateUserResponseDto();
    }

    @JsonProperty("success")
    private boolean success = true;

}
