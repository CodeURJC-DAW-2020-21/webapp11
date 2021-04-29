package es.urjc.code.daw.marketplace.api.order.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import es.urjc.code.daw.marketplace.util.TimeUtils;
import lombok.*;

import javax.annotation.PostConstruct;
import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FindOrderResponseDto {

    @JsonProperty("order_id")
    private Long orderId;

    @JsonProperty("category")
    private String category;

    @JsonProperty("purchase_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private Date purchase;

    @JsonProperty("expiration_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private Date expiration;

    @JsonProperty("is_expired")
    private Boolean isExpired;

    @JsonProperty("ram")
    private String ram;

    @JsonProperty("cores")
    private String cores;

    @JsonProperty("storage")
    private String storage;

    @JsonProperty("transfer")
    private String transfer;

    @JsonGetter("is_expired")
    public Boolean isExpired() {
        return TimeUtils.now().after(expiration);
    }

}
