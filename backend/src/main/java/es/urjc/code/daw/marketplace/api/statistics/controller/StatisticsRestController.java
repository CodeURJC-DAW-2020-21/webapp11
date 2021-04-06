package es.urjc.code.daw.marketplace.api.statistics.controller;

import es.urjc.code.daw.marketplace.api.jwt.dto.ValidateTokenResponseDto;
import es.urjc.code.daw.marketplace.api.statistics.dto.StatisticsResponseDto;
import es.urjc.code.daw.marketplace.domain.User;
import es.urjc.code.daw.marketplace.security.auth.AuthenticationService;
import es.urjc.code.daw.marketplace.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class StatisticsRestController {

    private static final String ROOT_ROUTE = "/api/statistics";

    private final ProductService productService;
    private final AuthenticationService authenticationService;

    public StatisticsRestController(ProductService productService,
                                    AuthenticationService authenticationService) {
        this.productService = productService;
        this.authenticationService = authenticationService;
    }

    @Operation(summary = "Finds the statistics associated to the sales")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "The sales statistics were found successfully",
                    content = {@Content(
                            schema = @Schema(implementation = StatisticsResponseDto.class)
                    )}
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "The requester is not authorized to perform this operation",
                    content = @Content
            ),
    })
    @RequestMapping(
            path = ROOT_ROUTE,
            method = RequestMethod.GET
    )
    public ResponseEntity<StatisticsResponseDto> findStatistics() {
        User loggedUser = authenticationService.getTokenUser();
        if(!loggedUser.isAdmin()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        List<Integer> currentWeekSales = productService.findSalesPerDayInWeek();
        List<Pair<String, Integer>> categoryWeeklyPurchases = productService.findCategoryToWeeklyPurchases();
        Long accumulatedCapital = productService.findAccumulatedCapital();

        StatisticsResponseDto response = StatisticsResponseDto.builder()
                .accumulatedCapital(accumulatedCapital)
                .categoryWeeklyPurchases(categoryWeeklyPurchases)
                .currentWeekSales(currentWeekSales)
            .build();

        return ResponseEntity.ok(response);
    }

}
