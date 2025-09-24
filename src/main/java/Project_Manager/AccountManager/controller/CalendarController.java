package Project_Manager.AccountManager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CalendarController {

    @GetMapping("/calendar/calendar")
    public String test() {

        return "/calendar/calendar";
    }

    @GetMapping("/calendar/add")
    public String addExpense() {

        return "/calendar/add";
    }
}
