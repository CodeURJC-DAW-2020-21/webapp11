package es.urjc.code.daw.marketplace.web.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EnableUserRequestDto {

    @JsonProperty("user_id")
    private Long userId;

}
