package ie.ait.tavares.pogo.application.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    @RequestMapping({"/", "/home"})
    public String getHome(Model model) {
        return "home";
    }
}
