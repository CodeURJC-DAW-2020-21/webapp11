package es.urjc.code.daw.marketplace.api.jwt.controller;

import es.urjc.code.daw.marketplace.api.common.RestResponseDto;
import es.urjc.code.daw.marketplace.api.jwt.dto.GenerateTokenRequestDto;
import es.urjc.code.daw.marketplace.api.jwt.dto.GenerateTokenResponseDto;
import es.urjc.code.daw.marketplace.api.jwt.dto.ValidateTokenResponseDto;
import es.urjc.code.daw.marketplace.domain.User;
import es.urjc.code.daw.marketplace.security.jwt.JwtTokenService;
import es.urjc.code.daw.marketplace.service.AuthenticationService;
import es.urjc.code.daw.marketplace.security.jwt.extractor.TokenExtractor;
import es.urjc.code.daw.marketplace.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.SneakyThrows;
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
    private final UserService userService;
    private final JwtTokenService tokenService;
    private final TokenExtractor tokenExtractor;

    public TokenRestController(AuthenticationService authenticationService,
                               UserService userService,
                               JwtTokenService tokenService,
                               TokenExtractor tokenExtractor) {
        this.authenticationService = authenticationService;
        this.userService = userService;
        this.tokenService = tokenService;
        this.tokenExtractor = tokenExtractor;
    }

    @Operation(summary = "Generates a token for the provided user credentials")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Returns the generated token",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "The provided credentials to generate a token were invalid",
                    content = @Content
            ),
    })
    @RequestMapping(path = BASE_ROUTE, method = RequestMethod.POST)
    public ResponseEntity<RestResponseDto> generateToken(@RequestBody GenerateTokenRequestDto request) {
        boolean authSuccess = true;
        try {
            authenticationService.authenticate(request.getEmail(), request.getPassword());
        } catch (Exception exception) {
            authSuccess = false;
        }
        User user = userService.findUserByEmail(request.getEmail());
        String generatedToken = tokenService.generateTokenFor(request.getEmail());
        boolean generationSuccessful = authSuccess && user != null && Strings.isNotEmpty(generatedToken);
        HttpStatus status = generationSuccessful ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        if(generationSuccessful) {
            GenerateTokenResponseDto content = GenerateTokenResponseDto.builder().token(generatedToken).userId(user.getId()).build();
            RestResponseDto response = RestResponseDto.builder().status(status).content(content).build();
            return new ResponseEntity<>(response, status);
        } else {
            final String message = "The provided login credentials were invalid";
            RestResponseDto response = RestResponseDto.builder().status(status).content(message).build();
            return new ResponseEntity<>(response, status);
        }
    }

    @Operation(summary = "Validates a given token")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Returns the validation response",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "No valid token was provided",
                    content = @Content
            ),
    })
    @RequestMapping(path = BASE_ROUTE, method = RequestMethod.GET)
    public ResponseEntity<RestResponseDto> validateToken() {
        String token = tokenExtractor.containsToken() ? tokenExtractor.extractToken() : Strings.EMPTY;
        boolean isTokenValid = tokenService.isTokenValid(token);
        User loggedUser = authenticationService.getTokenUser();
        boolean successful = loggedUser != null && isTokenValid;
        HttpStatus status = successful ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        if(successful) {
            ValidateTokenResponseDto content = ValidateTokenResponseDto.builder().token(token).isValid(true).userId(loggedUser.getId()).build();
            RestResponseDto response = RestResponseDto.builder().status(status).content(content).build();
            return new ResponseEntity<>(response, status);
        } else {
            final String message = "The token was invalid or no token was provided at all";
            RestResponseDto response = RestResponseDto.builder().status(status).content(message).build();
            return new ResponseEntity<>(response, status);
        }
    }

}
