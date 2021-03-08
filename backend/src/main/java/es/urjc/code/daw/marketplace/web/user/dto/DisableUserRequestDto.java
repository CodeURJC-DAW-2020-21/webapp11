package es.urjc.code.daw.marketplace.web.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DisableUserRequestDto {

    @JsonProperty("user_id")
    private Long userId;

}
