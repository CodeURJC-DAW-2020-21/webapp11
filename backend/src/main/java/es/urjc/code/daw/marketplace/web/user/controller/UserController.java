package es.urjc.code.daw.marketplace.web.user.controller;

import es.urjc.code.daw.marketplace.domain.User;
import es.urjc.code.daw.marketplace.security.user.UserPrincipal;
import es.urjc.code.daw.marketplace.service.PictureService;
import es.urjc.code.daw.marketplace.service.UserService;
import es.urjc.code.daw.marketplace.web.user.dto.RegisterUserRequestDto;
import es.urjc.code.daw.marketplace.web.user.dto.UpdateUserRequestDto;
import es.urjc.code.daw.marketplace.web.user.mapper.UserMapper;
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

@Controller
public class UserController {

    private final UserService userService;
    private final PictureService pictureService;
    private final UserMapper userMapper;

    public UserController(UserService userService,
                          PictureService pictureService,
                          UserMapper userMapper) {
        this.userService = userService;
        this.pictureService = pictureService;
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

        userService.registerUser(userMapper.asRegisterUser(request));

        final String viewIndicator = "isRegister";
        model.addAttribute(viewIndicator, "yes");

        model.addAttribute("message", "You have been registered successfully!");
        model.addAttribute("success", "yes");

        if(!Objects.isNull(userPrincipal)) {
            model.addAttribute("isLoggedIn", "yes");
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
        if(cannotPerform) return "error";

        User user = userService.findUserById(userId);
        model.addAttribute("user", user);
        model.addAttribute("isLoggedIn", "yes");
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

    @PreAuthorize("hasAnyRole('ROLE_CLIENT', 'ROLE_ADMIN')")
    @RequestMapping(path = "/user/{id}/update" , method = RequestMethod.POST)
    public String updateUser(@PathVariable("id") Long userId,
                             @RequestParam("image") MultipartFile profilePicture,
                             @AuthenticationPrincipal UserPrincipal userPrincipal,
                             UpdateUserRequestDto request,
                             Model model) {

        User currentUser = userService.findUserByEmail(userPrincipal.getUsername());
        boolean cannotPerform = !currentUser.isAdmin() && currentUser.getId().longValue() != userId.longValue();
        if(cannotPerform) return "error";

        User updateUser = userMapper.asUpdateUser(request);
        updateUser.setId(userId);

        if(!Objects.isNull(profilePicture) && !profilePicture.isEmpty()) {
            String filename = pictureService.savePicture(updateUser.getId(), profilePicture);
            updateUser.setProfilePictureFilename(filename);
        }

        User user = userService.updateUser(updateUser);

        model.addAttribute("isLoggedIn", "yes");
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
        model.addAttribute("spinner", "yes");

        return "flash";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(path = "/user/{id}/disable" , method = RequestMethod.POST)
    public String disableUser(@PathVariable("id") Long userId, Model model) {

        User user = userService.disableUser(userId);

        final String message = String.format("The user %s %s account has been disabled!", user.getFirstName(), user.getSurname());
        model.addAttribute("message", message);
        model.addAttribute("danger", "yes");
        model.addAttribute("spinner", "yes");

        return "flash";
    }

    @PreAuthorize("hasAnyRole('ROLE_CLIENT', 'ROLE_ADMIN')")
    @GetMapping(path = "/profile")
    public String profile(@AuthenticationPrincipal UserPrincipal userPrincipal, Model model) {

        model.addAttribute("isProfile", true);

        User currentUser = userService.findUserByEmail(userPrincipal.getUsername());

        return String.format("redirect:/user/%d", currentUser.getId());
    }

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
