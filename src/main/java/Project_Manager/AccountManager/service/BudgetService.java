package Project_Manager.AccountManager.service;

import Project_Manager.AccountManager.domain.BudgetDomain;
import Project_Manager.AccountManager.repository.BudgetRepository;
import Project_Manager.AccountManager.repository.CalendarRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BudgetService {

    private final BudgetRepository budgetRepository;
//    private final CanlendarRepository calendarRepository;

    public BudgetService(BudgetRepository budgetRepository) {this.budgetRepository = budgetRepository ;}

    // 총 예산 조회
    public List<BudgetDomain> findTotalAmount(Long user_id, LocalDate budget_date) {
        return budgetRepository.findAllBudgets(user_id, budget_date);
    }

    // 카테고리별 예산 조회
    public List<BudgetDomain> findCategoryAmount(Long user_id, Long category_id, LocalDate budget_date) {
        return budgetRepository.findAllBudgets(user_id, category_id, budget_date);
    }

    // 총 예산 등록 및 수정
    public void setTotalAmount(BudgetDomain budget) {
        List<BudgetDomain> totalBudgets = budgetRepository.findAllBudgets(
                budget.getUser_id(), 0L, budget.getBudget_date());

        // 1. 등록한 금액이 null일 경우 0L로 처리
            Long amount = budget.getExpected_amount();
            if(amount == null)
                amount = 0L;

        // 2. 최초 등록
        if (totalBudgets.isEmpty()) {
            BudgetDomain newTotalBudget = new BudgetDomain();
            newTotalBudget.setUser_id((budget.getUser_id()));
            newTotalBudget.setCategory_id(0L);
            newTotalBudget.setBudget_date(budget.getBudget_date());
            newTotalBudget.setExpected_amount(amount);

            budgetRepository.save(newTotalBudget);
        }

        // 3. 수정
        else {
            BudgetDomain budgetToUpdate = totalBudgets.get(0);
            budgetToUpdate.setExpected_amount(amount);
            budgetRepository.update(budgetToUpdate);
        }
    }

    // 카테고리별 예산 등록 및 수정
    public void updateCategoryAmount(BudgetDomain budget){
        List<BudgetDomain> categoryBudgets = budgetRepository.findAllBudgets(
                budget.getUser_id(),
                budget.getCategory_id(),
                budget.getBudget_date()
        );

        // 1. 최초 등록
        if(categoryBudgets.isEmpty())
            budgetRepository.save(budget);
        // 2. 수정
        else {
            BudgetDomain budgetToupdate = categoryBudgets.get(0);
            budgetToupdate.setExpected_amount(budget.getExpected_amount());
            budgetRepository.update(budgetToupdate);
        }
    }
}
