package kpi.prject.testing.testing.controller;

import kpi.prject.testing.testing.entity.User;
import kpi.prject.testing.testing.entity.enums.Role;
import kpi.prject.testing.testing.repository.UserRepository;
import kpi.prject.testing.testing.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.Optional;

@Controller
@RequestMapping("/home")
public class HomeController {
    private final UserService userService;

    public HomeController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("")
    public String getHome(Model model, Principal principal) {
        if (userService.getByUsername(principal.getName()).isPresent()){
            User user = userService.getByUsername(principal.getName()).get();
            if(user.getRole() == Role.ROLE_USER){
                model.addAttribute("username", principal.getName());
                return "home/userHome";
            } else if (user.getRole() == Role.ROLE_INSPECTOR){
                model.addAttribute("username", principal.getName());
                return "home/inspHome";
            }
        }
        return "redirect:/";
    }
}
