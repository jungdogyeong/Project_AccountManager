package Project_Manager.AccountManager.domain;

import lombok.Data;
import java.time.LocalDate;

@Data
public class BudgetDomain {
    private Long user_id;
    private Long budget_id;
    private Long category_id;
    private Long expected_amount;
    private LocalDate budget_date;

    public BudgetDomain() {
    }

    public BudgetDomain(Long user_id, Long budget_id, Long category_id, Long expected_amount, LocalDate budget_date) {
        this.user_id = user_id;
        this.budget_id = budget_id;
        this.category_id = category_id;
        this.expected_amount = expected_amount;
        this.budget_date = budget_date;
    }
}

