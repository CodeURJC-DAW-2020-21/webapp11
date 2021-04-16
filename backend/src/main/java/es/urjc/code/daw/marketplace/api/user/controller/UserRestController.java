package es.urjc.code.daw.marketplace.api.user.controller;

import es.urjc.code.daw.marketplace.api.statistics.dto.StatisticsResponseDto;
import es.urjc.code.daw.marketplace.api.user.dto.*;
import es.urjc.code.daw.marketplace.api.user.mapper.RestUserMapper;
import es.urjc.code.daw.marketplace.domain.User;
import es.urjc.code.daw.marketplace.security.auth.AuthenticationService;
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
import org.apache.commons.io.IOUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
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
    })
    @RequestMapping(
            path = BASE_ROUTE,
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<RegisterUserResponseDto> createUser(@RequestBody RegisterUserRequestDto request,
                                                              UriComponentsBuilder uriComponentsBuilder) {
        User requestUser = userMapper.asRegisterUser(request);
        User storedUser = userService.registerUser(requestUser);

        final String title = EmailMessageFactory.newWelcomeTitle();
        final String message = EmailMessageFactory.newWelcomeMessage(storedUser);
        emailService.sendEmail(storedUser.getEmail(), title, message);

        final String storedResourceUri = BASE_ROUTE + "/{id}";
        UriComponents components = uriComponentsBuilder.path(storedResourceUri).buildAndExpand(storedUser.getId());

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
        if(!loggedUser.isAdmin() && userId.longValue() != loggedUser.getId().longValue()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        User updateUser = userMapper.asUpdateUser(request);
        if(updateUser == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        updateUser.setId(userId);

        if(!Objects.isNull(request.getEncodedPicture()) && !Objects.isNull(request.getContentType())) {
            byte[] bytePicture = Base64.decodeBase64(request.getEncodedPicture());
            final String DEFAULT_FILENAME = "." + request.getContentType().split("/")[1];
            MultipartFile profilePicture = new DecodedBase64MultipartFile(DEFAULT_FILENAME, request.getContentType(), bytePicture);
            String filename = pictureService.savePicture(userId, profilePicture);
            updateUser.setProfilePictureFilename(filename);
        }

        userService.updateUser(updateUser, loggedUser.isAdmin());

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
    public ResponseEntity<FindUserResponseDto> findUser(@PathVariable("id") Long userId) {

        User loggedUser = authenticationService.getTokenUser();
        if(!loggedUser.isAdmin() && userId.longValue() != loggedUser.getId().longValue()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        User findUser = userService.findUserById(userId);
        if(findUser == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        FindUserResponseDto response = userMapper.asFindUserResponse(findUser);

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
        if(!loggedUser.isAdmin()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        List<User> users = userService.findAllUsers(PageRequest.of(page - 1, amount));
        if(users.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<FindUserResponseDto> response = users.stream().map(userMapper::asFindUserResponse).collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delivers an image in binary format")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "The image was successfully delivered",
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
                    description = "The requested image was not found",
                    content = @Content
            ),
    })
    @ResponseBody
    @RequestMapping(
            path = BASE_ROUTE + "/{id}/picture",
            method = RequestMethod.GET,
            produces = { MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE }
    )
    public byte[] getImage(@PathVariable("id") Long userId) throws Exception {

        User loggedUser = authenticationService.getTokenUser();
        if(!loggedUser.isAdmin() && userId.longValue() != loggedUser.getId().longValue()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        User toLoad = userService.findUserById(userId);
        if(toLoad.getProfilePictureFilename() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        File file = new File("user-profile-pictures/" + toLoad.getProfilePictureFilename());
        if(!file.exists()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        InputStream targetStream = new FileInputStream(file);
        return IOUtils.toByteArray(targetStream);
    }

}
