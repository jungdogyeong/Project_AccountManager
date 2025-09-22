package Project_Manager.AccountManager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class categoryController {

    @GetMapping("/category/category_main")
    public String test() {

        return "/category/category_main";
    }

    @GetMapping("/category/category_modify")
    public String addExpense() {

        return "/category/category_modify";
    }
}