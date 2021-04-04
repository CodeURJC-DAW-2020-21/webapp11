package es.urjc.code.daw.marketplace.api.statistics.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StatisticsResponseDto {

    @JsonProperty("accumulated_capital")
    Long accumulatedCapital;

    @JsonProperty("week_sales")
    List<Integer> currentWeekSales;

    @JsonProperty("category_purchases")
    List<Pair<String, Integer>> categoryWeeklyPurchases;

}
