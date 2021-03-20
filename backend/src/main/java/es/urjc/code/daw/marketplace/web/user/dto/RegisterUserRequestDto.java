package es.urjc.code.daw.marketplace.web.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterUserRequestDto {

    @Pattern(regexp = "^[a-zA-Z -]+$")
    @NotNull
    @NotEmpty
    private String firstName;

    @Pattern(regexp = "^[a-zA-Z -]+$")
    @NotNull
    @NotEmpty
    private String surname;

    @NotNull
    @NotEmpty
    private String address;

    @Size(max = 255)
    @Email
    @NotNull
    @NotEmpty
    private String email;

    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z]{8,}$")
    @NotNull
    @NotEmpty
    private String password;

}
