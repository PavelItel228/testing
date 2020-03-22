package kpi.prject.testing.testing.controller;

import kpi.prject.testing.testing.dto.UserDTO;
import kpi.prject.testing.testing.entity.User;
import kpi.prject.testing.testing.exceptions.InvalidUserException;
import kpi.prject.testing.testing.exceptions.UserExistsException;
import kpi.prject.testing.testing.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/accounts")
public class AccountsController {

    private final UserService userService;

    public AccountsController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Method for handling get requests to /accounts/login page </br>
     * Метод для обробки get запросів на сторінку accounts/login
     * @param error      optional query string for rendering page with error message </br>
     * @param logout     optional query string for rendering page with error logout message </br>
     * @param model      object for adding attributes for model and than put it in template html</br>
     * @return           html template</br>
     *                   html шаблон
     * @see kpi.prject.testing.testing.entity.User
     * @see kpi.prject.testing.testing.service.UserService
     */
    @GetMapping(value = "/login")
    public String getLoginForm(@RequestParam(value = "error", required = false) String error,
                               @RequestParam(value = "logout", required = false) String logout,
                               Model model) {
        model.addAttribute("error", error != null);
        model.addAttribute("logout", logout != null);
        return "accounts/login";
    }
    @GetMapping(value = "/registration")
    public String getRegistrationFrom(@ModelAttribute("user") UserDTO user){
        return "accounts/registration";
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public String addUser(@Valid @ModelAttribute("user") UserDTO user, BindingResult result) {
        User userFromDto = userService.getFromDto(user);
        try {
            userService.registration(userFromDto, result);
        } catch (UserExistsException | InvalidUserException e) {
            return "accounts/registration";
        }
        return "redirect:/accounts/login";
    }
}

