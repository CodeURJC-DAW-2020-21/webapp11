package es.urjc.code.daw.marketplace.api.jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidateTokenRequestDto {

    private String email;
    private String password;

}
