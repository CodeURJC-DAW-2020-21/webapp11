package es.urjc.code.daw.marketplace.api.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RestResponseDto {

    @JsonProperty("status_name")
    private String statusName;

    @JsonProperty("status_code")
    private Integer statusCode;

    @JsonProperty("content")
    @Builder.Default
    private Object content = StringUtils.EMPTY;

    public static class RestResponseDtoBuilder {

        public RestResponseDtoBuilder status(HttpStatus status) {
            this.statusName = status.name();
            this.statusCode = status.value();
            return this;
        }

    }

}
