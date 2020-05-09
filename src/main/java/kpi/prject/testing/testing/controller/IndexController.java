package kpi.prject.testing.testing.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
public class IndexController {

    @RequestMapping("/")
    public String getIndex(){
        return "index";
    }
}
