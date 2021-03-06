package es.urjc.code.daw.marketplace.web.user.controller;

import com.google.common.collect.Lists;
import es.urjc.code.daw.marketplace.domain.User;
import es.urjc.code.daw.marketplace.security.user.UserPrincipal;
import es.urjc.code.daw.marketplace.service.EmailService;
import es.urjc.code.daw.marketplace.service.PictureService;
import es.urjc.code.daw.marketplace.service.UserService;
import es.urjc.code.daw.marketplace.util.EmailContent;
import es.urjc.code.daw.marketplace.web.user.dto.RegisterUserRequestDto;
import es.urjc.code.daw.marketplace.web.user.dto.UpdateUserRequestDto;
import es.urjc.code.daw.marketplace.web.user.mapper.UserMapper;
import io.swagger.v3.oas.annotations.Operation;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;

/**
 * Represents the controller responsible for anything related
 * with the User class (logging in, registering, updating user info...)
 */


@Controller
public class UserController {

    private final UserService userService;
    private final PictureService pictureService;
    private final EmailService emailService;
    private final UserMapper userMapper;

    public UserController(UserService userService,
                          PictureService pictureService,
                          EmailService emailService,
                          UserMapper userMapper) {
        this.userService = userService;
        this.pictureService = pictureService;
        this.emailService = emailService;
        this.userMapper = userMapper;
    }

    @RequestMapping(path = "/login", method = RequestMethod.GET)
    public String loginUser(@ModelAttribute(value = "error") String errorMsg,
                            @AuthenticationPrincipal UserPrincipal userPrincipal,
                            Model model) {

        if(!Objects.isNull(userPrincipal)) {
            model.addAttribute("isLoggedIn", "yes");
            if(userPrincipal.getUser().isAdmin()) {
                model.addAttribute("isAdmin", "yes");
            }
            return "redirect:/";
        }

        final String errorKey = "error";
        boolean hasError = Strings.isNotBlank(errorMsg) && Strings.isNotEmpty(errorMsg);
        model.addAttribute(errorKey, hasError ? errorMsg : Strings.EMPTY);

        final String viewIndicator = "isLogin";
        model.addAttribute(viewIndicator, "yes");

        return "login";
    }

    @RequestMapping(
            path = "/register" ,
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
    )
    public String registerUser(@ModelAttribute("registerUser") RegisterUserRequestDto request,
                               @AuthenticationPrincipal UserPrincipal userPrincipal,
                               Model model) {

        User user = userService.registerUser(userMapper.asRegisterUser(request));

        String message = EmailContent.create()
                .addHeading("Thanks for registering")
                .addUnorderedList(
                        "Here is your profile information",
                        Lists.newArrayList(
                                "Name: " + user.getFirstName(),
                                "Surname: " + user.getSurname(),
                                "Email: " + user.getEmail()
                        )
                )
                .addHeading("and welcome to DAWHostServices!")
            .build();

        emailService.sendEmail(request.getEmail(), "Welcome to DAWHostServices", message);

        final String viewIndicator = "isRegister";
        model.addAttribute(viewIndicator, "yes");

        model.addAttribute("message", "You have been registered successfully!");
        model.addAttribute("success", "yes");

        if(!Objects.isNull(userPrincipal)) {
            model.addAttribute("isLoggedIn", "yes");
            model.addAttribute("loggedUser", userService.findUserByEmail(userPrincipal.getUsername()));
            if(userPrincipal.getUser().isAdmin()) {
                model.addAttribute("isAdmin", "yes");
            }
        }

        return "register";
    }

    @PreAuthorize("hasAnyRole('ROLE_CLIENT', 'ROLE_ADMIN')")
    @RequestMapping(path = "/user/{id}", method = RequestMethod.GET)
    public String findUser(@PathVariable("id") Long userId,
                           @AuthenticationPrincipal UserPrincipal userPrincipal,
                           Model model) {

        User currentUser = userService.findUserByEmail(userPrincipal.getUsername());
        boolean cannotPerform = !currentUser.isAdmin() && currentUser.getId().longValue() != userId.longValue();
        if(cannotPerform) return "redirect:/error";

        User user = userService.findUserById(userId);
        model.addAttribute("user", user);
        model.addAttribute("userId", user.getId());
        model.addAttribute("isLoggedIn", "yes");
        model.addAttribute("loggedUser", userService.findUserByEmail(userPrincipal.getUsername()));

        if(userPrincipal.getUser().isAdmin()) {
            model.addAttribute("isAdmin", "yes");
        }

        final String viewIndicator = "isProfile";
        model.addAttribute(viewIndicator, "yes");

        return "profile";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(path = "/users/{page}/{amount}", method = RequestMethod.GET)
    public String findUsers(@PathVariable("page") Integer page,
                            @PathVariable("amount") Integer amount,
                            Model model) {

        List<User> users = userService.findAllUsers(PageRequest.of(page - 1, amount));
        model.addAttribute("users", users);

        return "users";
    }

    /**
     * Updates the user info with the newly acquired information
     * from the form. Returns to the profile page, displaying
     * the newly updated information.
     */

    @PreAuthorize("hasAnyRole('ROLE_CLIENT', 'ROLE_ADMIN')")
    @RequestMapping(path = "/user/{userId}/update" , method = RequestMethod.POST)
    public String updateUser(@PathVariable("userId") Long userId,
                             @RequestParam("image") MultipartFile profilePicture,
                             @AuthenticationPrincipal UserPrincipal userPrincipal,
                             UpdateUserRequestDto request,
                             Model model) {

        User currentUser = userService.findUserByEmail(userPrincipal.getUsername());
        boolean cannotPerform = !currentUser.isAdmin() && currentUser.getId().longValue() != userId.longValue();
        if(cannotPerform) return "redirect:/error";

        User updateUser = userMapper.asUpdateUser(request);
        updateUser.setId(userId);

        if(!Objects.isNull(profilePicture) && !profilePicture.isEmpty()) {
            String filename = pictureService.savePicture(userId, profilePicture);
            updateUser.setProfilePictureFilename(filename);
        }

        User user = userService.updateUser(updateUser, currentUser.isAdmin());

        model.addAttribute("isLoggedIn", "yes");
        model.addAttribute("loggedUser", userService.findUserByEmail(userPrincipal.getUsername()));
        if(userPrincipal.getUser().isAdmin()) {
            model.addAttribute("isAdmin", "yes");
        }

        model.addAttribute("user", user);

        final String viewIndicator = "isProfile";
        model.addAttribute(viewIndicator, "yes");


        return "profile";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(path = "/user/{id}/enable", method = RequestMethod.POST)
    public String enableUser(@PathVariable("id") Long userId, Model model) {

        User user = userService.enableUser(userId);

        final String message = String.format("The user %s %s account has been enabled!", user.getFirstName(), user.getSurname());
        model.addAttribute("message", message);
        model.addAttribute("success", "yes");

        return "flash";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(path = "/user/{id}/disable" , method = RequestMethod.POST)
    public String disableUser(@PathVariable("id") Long userId, Model model) {

        User user = userService.disableUser(userId);

        final String message = String.format("The user %s %s account has been disabled!", user.getFirstName(), user.getSurname());
        model.addAttribute("message", message);
        model.addAttribute("danger", "yes");

        return "flash";
    }

    @PreAuthorize("hasAnyRole('ROLE_CLIENT', 'ROLE_ADMIN')")
    @GetMapping(path = "/profile")
    public String profile(@AuthenticationPrincipal UserPrincipal userPrincipal, Model model) {

        model.addAttribute("isProfile", true);

        User currentUser = userService.findUserByEmail(userPrincipal.getUsername());

        return String.format("redirect:/user/%d", currentUser.getId());
    }

    @Operation(hidden = true)
    @PreAuthorize("hasAnyRole('ROLE_CLIENT', 'ROLE_ADMIN')")
    @ResponseBody
    @GetMapping(path = "/user-profile-pictures/{id}",
                produces = { MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE })
    public byte[] getImage(@PathVariable("id") Long userId,
                           @AuthenticationPrincipal UserPrincipal userPrincipal) throws Exception {
        User currentUser = userService.findUserByEmail(userPrincipal.getUsername());
        boolean cannotPerform = !currentUser.isAdmin() && currentUser.getId().longValue() != userId.longValue();
        if(cannotPerform) throw new RuntimeException("Access denied");

        User toLoad = userService.findUserById(userId);
        File file = new File("user-profile-pictures/" + toLoad.getProfilePictureFilename());
        InputStream targetStream = new FileInputStream(file);
        return IOUtils.toByteArray(targetStream);
    }

}
