package Project_Manager.AccountManager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BudgetController {

    //네이게이터 클릭시 이동
    @GetMapping("/budget/budget")
    public String navBudget() {

        return "/budget/budget";
    }
}
