package es.urjc.code.daw.marketplace.api.jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class GenerateTokenResponseDto {

    public static GenerateTokenResponseDto create(String token, boolean successful) {
        return new GenerateTokenResponseDto(token, successful);
    }

    private String token;
    private boolean successful;

}
