package kpi.prject.testing.testing.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MyErrorController implements ErrorController {

    @RequestMapping("/denied")
    public  String errorAcessPage(){
        return "denied";
    }

    @RequestMapping("/error")
    public  String errorPage(){
        return "error";
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
