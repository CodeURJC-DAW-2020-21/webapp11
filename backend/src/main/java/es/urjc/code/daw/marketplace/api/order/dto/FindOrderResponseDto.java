package es.urjc.code.daw.marketplace.api.order.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class FindOrderResponseDto {

    @JsonProperty("order_id")
    private Long orderId;

    @JsonProperty("category")
    private String category;

    @JsonProperty("purchase_date")
    private Date purchase;

    @JsonProperty("expiration_date")
    private Date expiration;

    @JsonProperty("ram")
    private String ram;

    @JsonProperty("cores")
    private String cores;

    @JsonProperty("storage")
    private String storage;

    @JsonProperty("transfer")
    private String transfer;

}
