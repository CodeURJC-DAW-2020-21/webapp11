package es.urjc.code.daw.marketplace.api.user.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FindUserResponseDto {

    private Long id;

    private String firstName;

    private String surname;

    private String address;

    private String email;

    private Boolean isEnabled = true;

}
