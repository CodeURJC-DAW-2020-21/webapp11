package es.urjc.code.daw.marketplace.api.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FindUserResponseDto {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("surname")
    private String surname;

    @JsonProperty("address")
    private String address;

    @JsonProperty("email")
    private String email;

    @JsonProperty("enabled")
    private Boolean isEnabled = true;

    @JsonProperty("is_admin")
    private Boolean isAdmin = false;

    @JsonProperty("encoded_image")
    private String encodedImage = "";

}
