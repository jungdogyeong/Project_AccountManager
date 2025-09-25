package Project_Manager.AccountManager.service;

import Project_Manager.AccountManager.repository.BudgetRepository;
import org.springframework.stereotype.Service;

@Service
public class BudgetService {
    private BudgetRepository budgetRepository;

    public BudgetService(BudgetRepository budgetRepository) {
        this.budgetRepository = budgetRepository;
    }

    public int getTotal(int member_id) {
        return budgetRepository.getTotalExpense(member_id);
    }
}
