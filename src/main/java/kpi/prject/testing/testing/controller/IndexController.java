package kpi.prject.testing.testing.controller;

import kpi.prject.testing.testing.dto.UserDTO;
import kpi.prject.testing.testing.entity.User;
import kpi.prject.testing.testing.entity.enums.ReportStatus;
import kpi.prject.testing.testing.entity.enums.Role;
import kpi.prject.testing.testing.exceptions.InvalidUserException;
import kpi.prject.testing.testing.service.ReportService;
import kpi.prject.testing.testing.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.Optional;

@Controller
public class IndexController {

    private final UserService userService;

    public IndexController(UserService userService, ReportService reportService) {
        this.userService = userService;
    }

    @RequestMapping("/")
    public String getIndex(Principal principal, Model model) {
        try {
            User user = userService.getByUsername(principal.getName()).orElseThrow(InvalidUserException::new);
            if (user.getRole() == Role.ROLE_USER) {
                model.addAttribute("url", "/userHome");
            } else if (user.getRole() == Role.ROLE_INSPECTOR) {
                model.addAttribute("url", "/inspHome");
            }
        } catch (InvalidUserException | NullPointerException ignored) {
        }
        return "index";
    }
}
