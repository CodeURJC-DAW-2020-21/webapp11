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
    public String login(@ModelAttribute(value = "error") String errorMsg, Model model) {

        final String errorKey = "error";
        boolean hasError = Strings.isNotBlank(errorMsg) && Strings.isNotEmpty(errorMsg);
        model.addAttribute(errorKey, hasError ? errorMsg : Strings.EMPTY);

        return "login";
    }

    @RequestMapping(path = "/register" , method = RequestMethod.POST)
    public String registerUser(@RequestBody RegisterUserRequestDto request, Model model) {

        User user = userService.registerUser(userMapper.asRegisterUser(request));
        model.addAttribute("first_name", user.getFirstName());
        model.addAttribute("surname", user.getSurname());

        return "register";
    }

    @RequestMapping(path = "/profile/{id}" , method = RequestMethod.POST)
    public String findUser(@PathVariable("id") Long userId,
                           @AuthenticationPrincipal UserPrincipal userPrincipal,
                           Model model) {

        User loggedInUser = userService.findUserByEmail(userPrincipal.getUsername());
        if(loggedInUser.getId().longValue() != userId.longValue() && !loggedInUser.isAdmin()) {
            model.addAttribute("error", "You don't have permission to perform that action");
        }

        User user = userService.findUserById(userId);
        user.setPassword(Strings.EMPTY);
        model.addAttribute("user", user);

        return Strings.EMPTY; // TODO: Replace with pertinent view
    }

    @RequestMapping(path = "/user/update" , method = RequestMethod.POST)
    public String updateUser(@RequestBody UpdateUserRequestDto request,
                             @AuthenticationPrincipal UserPrincipal userPrincipal,
                             Model model) {

        User loggedInUser = userService.findUserByEmail(userPrincipal.getUsername());
        if(loggedInUser.getId().longValue() != request.getUserId().longValue() && !loggedInUser.isAdmin()) {
            model.addAttribute("error", "You don't have permission to perform that action");
        }

        User user = userService.updateUser(userMapper.asUpdateUser(request));
        final String message = String.format("Successfully updated %s account", user.getEmail());
        model.addAttribute("message", message);

        return Strings.EMPTY; // TODO: Replace with pertinent view
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(path = "/user/enable/{id}" , method = RequestMethod.POST)
    public String enableUser(@PathVariable("id") Long userId, Model model) {

        User user = userService.enableUser(userId);
        model.addAttribute("successful", true);
        final String message = String.format("The user account with email %s has been enabled", user.getEmail());
        model.addAttribute("message", message);

        return Strings.EMPTY; // TODO: Replace with pertinent view
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(path = "/user/disable/{id}" , method = RequestMethod.POST)
    public String disableUser(@PathVariable("id") Long userId, Model model) {

        User user = userService.disableUser(userId);
        model.addAttribute("successful", true);
        final String message = String.format("The user account with email %s has been disabled", user.getEmail());
        model.addAttribute("message", message);

        return Strings.EMPTY; // TODO: Replace with pertinent view
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(path = "/user/find/{page}/{amount}", method = RequestMethod.POST)
    public String findUsers(@PathVariable("page") Integer page,
                            @PathVariable("amount") Integer amount,
                            Model model) {

        List<User> users = userService.findAllUsers(PageRequest.of(page - 1, amount));
        users.forEach(user -> user.setPassword(Strings.EMPTY));
        model.addAttribute("users", users);

        return Strings.EMPTY; // TODO: Replace with pertinent view
    }

}
