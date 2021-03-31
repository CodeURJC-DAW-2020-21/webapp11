package es.urjc.code.daw.marketplace.api.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRequestDto {

    @JsonProperty("encoded_picture")
    private String encodedPicture;

    @JsonProperty("content_type")
    private String contentType;

    @Pattern(regexp = "^[a-zA-Z -]+$")
    @NotNull
    @NotEmpty
    @JsonProperty("first_name")
    private String firstName;

    @Pattern(regexp = "^[a-zA-Z -]+$")
    @NotNull
    @NotEmpty
    @JsonProperty("surname")
    private String surname;

    @NotNull
    @NotEmpty
    @JsonProperty("address")
    private String address;

    @Size(max = 255)
    @Email
    @NotNull
    @NotEmpty
    @JsonProperty("email")
    private String email;

    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z]{8,}$")
    @JsonProperty("password")
    private String password;

}

