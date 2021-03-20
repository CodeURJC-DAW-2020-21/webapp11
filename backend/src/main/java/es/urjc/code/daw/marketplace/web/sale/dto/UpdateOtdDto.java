package es.urjc.code.daw.marketplace.web.sale.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UpdateOtdDto {

    private String startDate;

    private String stopDate;

    private String discountPercentage;

    private String productId;

}
