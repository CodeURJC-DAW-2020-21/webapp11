package es.urjc.code.daw.marketplace.api.jwt.controller;

import es.urjc.code.daw.marketplace.api.jwt.dto.GenerateTokenRequestDto;
import es.urjc.code.daw.marketplace.api.jwt.dto.GenerateTokenResponseDto;
import es.urjc.code.daw.marketplace.api.jwt.dto.ValidateTokenResponseDto;
import es.urjc.code.daw.marketplace.domain.User;
import es.urjc.code.daw.marketplace.security.jwt.JwtTokenService;
import es.urjc.code.daw.marketplace.security.auth.AuthenticationService;
import es.urjc.code.daw.marketplace.security.jwt.extractor.TokenExtractor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TokenRestController {

    private static final String BASE_ROUTE = "/api/tokens";

    private final AuthenticationService authenticationService;
    private final JwtTokenService tokenService;
    private final TokenExtractor tokenExtractor;

    public TokenRestController(AuthenticationService authenticationService,
                               JwtTokenService tokenService,
                               TokenExtractor tokenExtractor) {
        this.authenticationService = authenticationService;
        this.tokenService = tokenService;
        this.tokenExtractor = tokenExtractor;
    }

    @Operation(summary = "Generates a token for the provided user credentials")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Returns the generated token",
                    content = {@Content(
                            schema = @Schema(implementation = GenerateTokenResponseDto.class)
                    )}
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "The provided credentials to generate a token were invalid",
                    content = @Content
            ),
    })
    @RequestMapping(path = { BASE_ROUTE + "/generate" }, method = RequestMethod.POST)
    public ResponseEntity<GenerateTokenResponseDto> generateToken(@RequestBody GenerateTokenRequestDto request) {
        authenticationService.authenticate(request.getEmail(), request.getPassword());
        String generatedToken = tokenService.generateTokenFor(request.getEmail());
        boolean generationSuccessful = Strings.isNotEmpty(generatedToken);
        GenerateTokenResponseDto response = GenerateTokenResponseDto.create(generatedToken, generationSuccessful);
        HttpStatus status = generationSuccessful ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(response, status);
    }

    @Operation(summary = "Validates a given token")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Returns the validation response",
                    content = {@Content(
                            schema = @Schema(implementation = ValidateTokenResponseDto.class)
                    )}
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "No valid token was provided",
                    content = @Content
            ),
    })
    @RequestMapping(path = { BASE_ROUTE + "/validate" }, method = RequestMethod.POST)
    public ResponseEntity<ValidateTokenResponseDto> validateToken() {
        String token = tokenExtractor.containsToken() ? tokenExtractor.extractToken() : Strings.EMPTY;
        boolean isTokenValid = tokenService.isTokenValid(token);
        User loggedUser = authenticationService.getTokenUser();
        ValidateTokenResponseDto response = ValidateTokenResponseDto.create(loggedUser.getId(), token, isTokenValid);
        HttpStatus status = isTokenValid ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(response, status);
    }

}
