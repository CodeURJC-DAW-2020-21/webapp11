package es.urjc.code.daw.marketplace.api.user.controller;

import es.urjc.code.daw.marketplace.api.common.RestResponseDto;
import es.urjc.code.daw.marketplace.api.user.dto.*;
import es.urjc.code.daw.marketplace.api.user.mapper.RestUserMapper;
import es.urjc.code.daw.marketplace.domain.User;
import es.urjc.code.daw.marketplace.service.AuthenticationService;
import es.urjc.code.daw.marketplace.service.EmailService;
import es.urjc.code.daw.marketplace.service.PictureService;
import es.urjc.code.daw.marketplace.service.UserService;
import es.urjc.code.daw.marketplace.util.DecodedBase64MultipartFile;
import es.urjc.code.daw.marketplace.util.EmailMessageFactory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@CrossOrigin("*")
@RestController
public class UserRestController {

    private static final String BASE_ROUTE = "/api/users";

    private final RestUserMapper userMapper;
    private final UserService userService;
    private final EmailService emailService;
    private final PictureService pictureService;
    private final AuthenticationService authenticationService;

    public UserRestController(RestUserMapper userMapper,
                              UserService userService,
                              EmailService emailService,
                              PictureService pictureService,
                              AuthenticationService authenticationService) {
        this.userMapper = userMapper;
        this.userService = userService;
        this.emailService = emailService;
        this.pictureService = pictureService;
        this.authenticationService = authenticationService;
    }

    @Operation(summary = "Registers a new user")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "The new user creation was successful",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "The provided input was not valid",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "The requester is not authorized to perform this operation",
                    content = @Content
            ),
    })
    @RequestMapping(
            path = BASE_ROUTE,
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<RestResponseDto> createUser(@RequestBody RegisterUserRequestDto request,
                                                      UriComponentsBuilder uriComponentsBuilder) {
        try {
            // Build the domain user from the request
            User requestUser = userMapper.asRegisterUser(request);
            // Save the built user
            User storedUser = userService.registerUser(requestUser);
            // Construct the user welcome email message
            final String emailTitle = EmailMessageFactory.newWelcomeTitle();
            final String emailMessage = EmailMessageFactory.newWelcomeMessage(storedUser);
            // Send the message
            try {
                emailService.sendEmail(storedUser.getEmail(), emailTitle, emailMessage);
            } catch(Exception exception) {
                final String message = "The welcome email message sending has failed";
                RestResponseDto response = RestResponseDto.builder().status(HttpStatus.INTERNAL_SERVER_ERROR).content(message).build();
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            // Create the location header to be returned
            final String storedResourceUri = BASE_ROUTE + "/{id}";
            UriComponents components = uriComponentsBuilder.path(storedResourceUri).buildAndExpand(storedUser.getId());
            // Return a successful response with the location header
            FindUserResponseDto findUser = userMapper.asFindUserResponse(storedUser, StringUtils.EMPTY);
            RestResponseDto response = RestResponseDto.builder().status(HttpStatus.OK).content(findUser).build();
            return ResponseEntity.created(components.toUri()).body(response);
        } catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    @Operation(summary = "Updates the information of a user")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "The information update of the user was successful",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "The provided input was not valid",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "The requester is not authorized to perform this operation",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "There was no user associated with the provided user id",
                    content = @Content
            ),
    })
    @RequestMapping(
            path = BASE_ROUTE + "/{id}",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<RestResponseDto> updateUser(@PathVariable("id") Long userId,
                                                      @RequestBody UpdateUserRequestDto request) {
        User loggedUser = authenticationService.getTokenUser();
        if(loggedUser == null) {
            final String message = "The token was invalid or no token was provided at all";
            RestResponseDto response = RestResponseDto.builder().status(HttpStatus.UNAUTHORIZED).content(message).build();
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
        // Ensure the logged in user has permissions to update
        if(!loggedUser.isAdmin() && userId.longValue() != loggedUser.getId().longValue()) {
            final String message = "You have no permission to perform this operation (only the order owner or the admin)";
            RestResponseDto response = RestResponseDto.builder().status(HttpStatus.UNAUTHORIZED).content(message).build();
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
        // Map the user to update to a domain entity
        User updateUser = userMapper.asUpdateUser(request);
        if(updateUser == null) {
            final String message = "There is no user associated to the provided user id";
            RestResponseDto response = RestResponseDto.builder().status(HttpStatus.NOT_FOUND).content(message).build();
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        // Set the id to the the user to be modified id
        updateUser.setId(userId);
        // Ensure the picture to be updated is present in the request and if so update the picture
        if(!Objects.isNull(request.getEncodedPicture()) && !Objects.isNull(request.getContentType())) {
            byte[] bytePicture = Base64.decodeBase64(request.getEncodedPicture());
            final String DEFAULT_FILENAME = "." + request.getContentType().split("/")[1];
            // Decode the base64 picture into a stream of bytes
            MultipartFile profilePicture = new DecodedBase64MultipartFile(DEFAULT_FILENAME, request.getContentType(), bytePicture);
            String filename = pictureService.savePicture(userId, profilePicture);
            // Update the profile picture
            updateUser.setProfilePictureFilename(filename);
        }
        // Update the user data
        User storedUser = userService.updateUser(updateUser, loggedUser.isAdmin());
        // Send a successful response
        FindUserResponseDto findUser = userMapper.asFindUserResponse(storedUser, pictureService.getEncodedPicture(storedUser));
        RestResponseDto response = RestResponseDto.builder().status(HttpStatus.OK).content(findUser).build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Finds the user information")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Returns the information of a requested user",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "The requester is not authorized to perform this operation",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "There was no user associated with the provided user id",
                    content = @Content
            ),
    })
    @RequestMapping(
            path = BASE_ROUTE + "/{id}",
            method = RequestMethod.GET
    )
    public ResponseEntity<RestResponseDto> findUser(@PathVariable("id") Long userId) throws Exception {
        User loggedUser = authenticationService.getTokenUser();
        if(loggedUser == null) {
            final String message = "The token was invalid or no token was provided at all";
            RestResponseDto response = RestResponseDto.builder().status(HttpStatus.UNAUTHORIZED).content(message).build();
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
        // Ensure the logged in user has permissions to update
        if(!loggedUser.isAdmin() && userId.longValue() != loggedUser.getId().longValue()) {
            final String message = "You have no permission to perform this operation (only the order owner or the admin)";
            RestResponseDto response = RestResponseDto.builder().status(HttpStatus.UNAUTHORIZED).content(message).build();
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
        // Ensure the user associated to the provided id exists
        User findUser = userService.findUserById(userId);
        if(findUser == null) {
            final String message = "There is no user associated to the provided user id";
            RestResponseDto response = RestResponseDto.builder().status(HttpStatus.NOT_FOUND).content(message).build();
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        // Find the base64 encoded picture
        String encodedImage = pictureService.getEncodedPicture(findUser);
        // Build the response with the user and encoded image
        FindUserResponseDto content = userMapper.asFindUserResponse(findUser, encodedImage);
        // Send successful response
        RestResponseDto response = RestResponseDto.builder().status(HttpStatus.OK).content(content).build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Finds a paginated list of users")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "The list of users was retrieved successfully",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "The requester is not authorized to perform this operation",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "There are no users available for the provided pagination information",
                    content = @Content
            ),
    })
    @RequestMapping(
            path = BASE_ROUTE,
            method = RequestMethod.GET
    )
    public ResponseEntity<RestResponseDto> findUsers(@RequestParam("page") Integer page,
                                                               @RequestParam("amount") Integer amount) {
        User loggedUser = authenticationService.getTokenUser();
        if(loggedUser == null) {
            final String message = "The token was invalid or no token was provided at all";
            RestResponseDto response = RestResponseDto.builder().status(HttpStatus.UNAUTHORIZED).content(message).build();
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
        // Ensure the requester is an admin
        if(!loggedUser.isAdmin()) {
            final String message = "You have no permission to perform this operation (only the order owner or the admin)";
            RestResponseDto response = RestResponseDto.builder().status(HttpStatus.UNAUTHORIZED).content(message).build();
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
        // Find all users
        List<User> users = userService.findAllUsers(PageRequest.of(page - 1, amount));
        if(users.isEmpty()) {
            final String message = "There are no users to be returned (user list is empty)";
            RestResponseDto response = RestResponseDto.builder().status(HttpStatus.NOT_FOUND).content(message).build();
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        // Map users to DTO's
        List<FindUserResponseDto> content = users.stream().map(user -> {
            String encodedImage = pictureService.getEncodedPicture(user);
            return userMapper.asFindUserResponse(user, encodedImage);
        }).collect(Collectors.toList());
        // Send successful response
        RestResponseDto response = RestResponseDto.builder().status(HttpStatus.OK).content(content).build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
