package es.urjc.code.daw.marketplace.api.sale.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateSaleRequestDto {

    @JsonProperty("product_id")
    private Long productId;

    @JsonProperty("start")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date startDate;

    @JsonProperty("stop")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date stopDate;

    @JsonProperty("discount")
    private Integer discount;

    @JsonProperty("amount")
    private Integer amount;

    @JsonProperty("enabled")
    private Boolean isEnabled;

}
