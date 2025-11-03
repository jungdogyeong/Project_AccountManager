package Project_Manager.AccountManager.controller;


import Project_Manager.AccountManager.domain.BudgetDomain;
import Project_Manager.AccountManager.service.BudgetService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/budget")
public class BudgetController {
        private final BudgetService budgetService;

        public BudgetController(BudgetService budgetService){
            this.budgetService = budgetService;
        }

        // 메인 페이지 처리
        @GetMapping("/budget")
        public String budgetHome(Model model, @RequestParam("user_id") Long user_id){
            LocalDate today = LocalDate.now();

            List<BudgetDomain> totalBudgetList = budgetService.findTotalAmount(user_id,
                    LocalDate.of(today.getYear(), today.getMonth(), 1));

            model.addAttribute("user_id", user_id);
            model.addAttribute("totalBudgetList", totalBudgetList);

            return "/budget/budget";
        }

        // 총 예산 설정 페이지 처리
        @GetMapping("/budgetTotal")
        public String budgetTotalForm(Model model, @RequestParam("user_id") Long user_id){

            LocalDate firstDayOfMonth = LocalDate.now().withDayOfMonth(1);
            List<BudgetDomain> totalBudgets = budgetService.findTotalAmount(user_id,
                    firstDayOfMonth);

            if(!totalBudgets.isEmpty())
                model.addAttribute("budget", totalBudgets.get(0));
            else {
                BudgetDomain newBudget = new BudgetDomain();
                newBudget.setUser_id(user_id);
                newBudget.setBudget_date(firstDayOfMonth);
                model.addAttribute("budget", newBudget);
            }

            return "/budget/budgetTotal";
        }

        // 카테고리별 예산 설정 페이지 처리
        @GetMapping("/budgetModify")
        public String budgetModifyForm(Model model, @RequestParam("user_id") Long user_id){
            model.addAttribute("user_id", user_id);

            return "/budget/budgetModify";
        }

        // 총 예산 등록 및 수정 처리
        @PostMapping("/save/total")
        public String saveTotalBudget(@ModelAttribute BudgetDomain budget,
                                      RedirectAttributes redirectAttributes) {
            try {
                budgetService.setTotalAmount(budget);
                redirectAttributes.addFlashAttribute("message",
                        "총 예산이 성공적으로 저장되었습니다.");
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("error",
                        "총 예산 저장에 실패했습니다.");
            }

            return "redirect:/budget/budget?user_id=" + budget.getUser_id();
        }

        // 카테고리별 예산 등록 및 수정 처리
        @PostMapping("/save/category")
        public String saveCategoryBudget(@ModelAttribute BudgetDomain budget,
                                         RedirectAttributes redirectAttributes) {
            try {
                budgetService.updateCategoryAmount(budget);
                redirectAttributes.addFlashAttribute("message",
                        "카테고리 예산이 성공적으로 저장되었습니다.");
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("error",
                        "카테고리 예산 저장에 실패했습니다.");
            }

            return "redirect:/budget/budget?user_id=" + budget.getUser_id();
        }

}
