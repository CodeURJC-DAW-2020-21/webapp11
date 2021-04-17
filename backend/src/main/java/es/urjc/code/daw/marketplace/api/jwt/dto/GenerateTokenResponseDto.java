package es.urjc.code.daw.marketplace.api.jwt.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class GenerateTokenResponseDto {

    @JsonProperty("user_id")
    @Builder.Default
    private Long userId = -1L;

    @JsonProperty("token")
    @Builder.Default
    private String token = StringUtils.EMPTY;


}
