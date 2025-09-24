package Project_Manager.AccountManager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ExpenseController {

    //네비게이터 클릭시 이동
    @GetMapping("/expense/expense")
    public String navExpense(){

        return "/expense/expense";
    }
}
