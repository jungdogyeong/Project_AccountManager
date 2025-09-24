package Project_Manager.AccountManager.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class navigatorController {

    @GetMapping("/nav/navigator")
    public String navigator(){

        return "/nav/navigator";
    }
}
