package Project_Manager.AccountManager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CategoryController {

    @GetMapping("/category/categoryMain")
    public String test() {

        return "/category/categoryMain";
    }

    @GetMapping("/category/categoryModify")
    public String addExpense() {

        return "/category/categoryModify";
    }
}