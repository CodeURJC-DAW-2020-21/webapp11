package es.urjc.code.daw.marketplace.web.user.controller;

import es.urjc.code.daw.marketplace.domain.User;
import es.urjc.code.daw.marketplace.security.user.UserPrincipal;
import es.urjc.code.daw.marketplace.service.UserService;
import es.urjc.code.daw.marketplace.web.user.dto.RegisterUserRequestDto;
import es.urjc.code.daw.marketplace.web.user.dto.UpdateUserRequestDto;
import es.urjc.code.daw.marketplace.web.user.mapper.UserMapper;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @RequestMapping(path = "/login", method = RequestMethod.GET)
    public String loginUser(@ModelAttribute(value = "error") String errorMsg, Model model) {

        final String errorKey = "error";
        boolean hasError = Strings.isNotBlank(errorMsg) && Strings.isNotEmpty(errorMsg);
        model.addAttribute(errorKey, hasError ? errorMsg : Strings.EMPTY);

        final String viewIndicator = "isLogin";
        model.addAttribute(viewIndicator, "yes");

        return "login";
    }

    @RequestMapping(path = "/register" , method = RequestMethod.POST)
    public String registerUser(@RequestBody RegisterUserRequestDto request, Model model) {

        userService.registerUser(userMapper.asRegisterUser(request));

        final String viewIndicator = "isRegister";
        model.addAttribute(viewIndicator, "yes");

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
                             @AuthenticationPrincipal UserPrincipal userPrincipal,
                             UpdateUserRequestDto request,
                             Model model) {

        User currentUser = userService.findUserByEmail(userPrincipal.getUsername());
        boolean cannotPerform = !currentUser.isAdmin() && currentUser.getId().longValue() != userId.longValue();
        if(cannotPerform) return "error";

        User updateUser = userMapper.asUpdateUser(request);
        updateUser.setId(userId);

        User user = userService.updateUser(updateUser);
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

}
