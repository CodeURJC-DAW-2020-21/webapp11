package es.urjc.code.daw.marketplace.web.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRequestDto {


    private String firstName;
    private String surname;
    private String address;
    private String email;
    private String password;
    private String profilePictureUrl;
    private Boolean isEnabled;
    private Boolean isLocked;

}
