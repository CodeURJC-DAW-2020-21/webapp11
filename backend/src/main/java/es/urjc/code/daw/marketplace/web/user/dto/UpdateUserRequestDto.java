package es.urjc.code.daw.marketplace.web.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRequestDto {

    @Pattern(regexp = "^[a-zA-Z -]+$")
    @NotNull
    @NotEmpty
    private String firstName;

    @Pattern(regexp = "^[a-zA-Z -]+$")
    @NotNull
    @NotEmpty
    private String surname;

    @Pattern(regexp = "^[\\w\\s.-]+\\d+,\\s*[\\w\\s.-]+$")
    @NotNull
    @NotEmpty
    private String address;

    @Size(max = 255)
    @Email
    @NotNull
    @NotEmpty
    private String email;

    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z]{8,}$")
    private String password;

}
