package es.urjc.code.daw.marketplace.api.order.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateOrderRequestDto {

    @JsonProperty("months")
    private Integer monthsRenewed;

    @JsonProperty("cancel")
    private Boolean cancelOrder;

    public boolean shouldCancel() {
        if(cancelOrder == null) return false;
        return cancelOrder;
    }

    public boolean shouldRenew() {
        if(monthsRenewed == null) return false;
        return monthsRenewed >= 1;
    }

}
