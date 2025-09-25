package Project_Manager.AccountManager.controller;

import Project_Manager.AccountManager.service.BudgetService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class BudgetController {

    private final BudgetService budgetService;

    public BudgetController(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    @GetMapping("/budget/budget")
    public String getTotalAmount(
            @RequestParam("member_id") int member_id,
            Model model) {
        int amount = budgetService.getTotal(member_id);

        model.addAttribute("amount", amount);

        return "/budget/budget";
    }

}
