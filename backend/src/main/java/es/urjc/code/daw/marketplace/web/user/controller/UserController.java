package es.urjc.code.daw.marketplace.web.user.controller;

import es.urjc.code.daw.marketplace.domain.User;
import es.urjc.code.daw.marketplace.service.UserService;
import es.urjc.code.daw.marketplace.web.user.dto.RegisterUserRequestDto;
import es.urjc.code.daw.marketplace.web.user.mapper.UserMapper;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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

}
