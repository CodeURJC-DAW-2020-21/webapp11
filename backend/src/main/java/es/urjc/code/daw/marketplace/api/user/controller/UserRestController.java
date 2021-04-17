package es.urjc.code.daw.marketplace.api.user.controller;

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
                    content = {@Content(
                            schema = @Schema(implementation = RegisterUserResponseDto.class)
                    )}
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
    public ResponseEntity<RegisterUserResponseDto> createUser(@RequestBody RegisterUserRequestDto request,
                                                              UriComponentsBuilder uriComponentsBuilder) {
        // Build the domain user from the request
        User requestUser = userMapper.asRegisterUser(request);
        // Save the built user
        User storedUser = userService.registerUser(requestUser);
        // Construct the user welcome email message
        final String title = EmailMessageFactory.newWelcomeTitle();
        final String message = EmailMessageFactory.newWelcomeMessage(storedUser);
        // Send the message
        emailService.sendEmail(storedUser.getEmail(), title, message);
        // Create the location header to be returned
        final String storedResourceUri = BASE_ROUTE + "/{id}";
        UriComponents components = uriComponentsBuilder.path(storedResourceUri).buildAndExpand(storedUser.getId());
        // Return a successful response with the location header
        return ResponseEntity.created(components.toUri()).body(RegisterUserResponseDto.successful());
    }

    @Operation(summary = "Updates the information of a user")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "The information update of the user was successful",
                    content = {@Content(
                            schema = @Schema(implementation = UpdateUserResponseDto.class)
                    )}
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
    public ResponseEntity<UpdateUserResponseDto> updateUser(@PathVariable("id") Long userId,
                                                            @RequestBody UpdateUserRequestDto request) {
        User loggedUser = authenticationService.getTokenUser();
        // Ensure the logged in user has permissions to update
        if(!loggedUser.isAdmin() && userId.longValue() != loggedUser.getId().longValue()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        // Map the user to update to a domain entity
        User updateUser = userMapper.asUpdateUser(request);
        if(updateUser == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
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
        userService.updateUser(updateUser, loggedUser.isAdmin());
        // Send a successful response
        return ResponseEntity.ok(UpdateUserResponseDto.successful());
    }

    @Operation(summary = "Finds the user information")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Returns the information of a requested user",
                    content = {@Content(
                            schema = @Schema(implementation = FindUserResponseDto.class)
                    )}
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
    public ResponseEntity<FindUserResponseDto> findUser(@PathVariable("id") Long userId) throws Exception {
        User loggedUser = authenticationService.getTokenUser();
        // Ensure the logged in user has permissions to update
        if(!loggedUser.isAdmin() && userId.longValue() != loggedUser.getId().longValue()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        // Ensure the user associated to the provided id exists
        User findUser = userService.findUserById(userId);
        if(findUser == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        // Find the base64 encoded picture
        String encodedImage = pictureService.getEncodedPicture(findUser);
        // Build the response with the user and encoded image
        FindUserResponseDto response = userMapper.asFindUserResponse(findUser, encodedImage);
        // Send successful response
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Finds a paginated list of users")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "The list of users was retrieved successfully",
                    content = {@Content(
                            array = @ArraySchema(schema = @Schema(implementation = FindUserResponseDto.class))
                    )}
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
    public ResponseEntity<List<FindUserResponseDto>> findUsers(@RequestParam("page") Integer page,
                                                               @RequestParam("amount") Integer amount) {
        User loggedUser = authenticationService.getTokenUser();
        // Ensure the requester is an admin
        if(!loggedUser.isAdmin()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        // Find all users
        List<User> users = userService.findAllUsers(PageRequest.of(page - 1, amount));
        if(users.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        // Map users to DTO's
        List<FindUserResponseDto> response = users.stream().map(user -> {
            String encodedImage = pictureService.getEncodedPicture(user);
            return userMapper.asFindUserResponse(user, encodedImage);
        }).collect(Collectors.toList());
        // Send successful response
        return ResponseEntity.ok(response);
    }

}
