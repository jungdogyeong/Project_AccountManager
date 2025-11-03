package Project_Manager.AccountManager.repository;

import Project_Manager.AccountManager.domain.BudgetDomain;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class BudgetRepository {

    private final JdbcTemplate jdbcTemplate;

    public BudgetRepository(JdbcTemplate jdbcTemplate) {this.jdbcTemplate = jdbcTemplate;}

    // RowMapper 정의
    private RowMapper<BudgetDomain> budgetRowMapper = (rs, rowNum) -> {
        BudgetDomain budget = new BudgetDomain();
        budget.setBudget_id(rs.getLong("budget_id"));
        budget.setUser_id(rs.getLong("user_id"));
        budget.setCategory_id(rs.getLong("category_id"));
        budget.setExpected_amount(rs.getLong("expected_amount"));
        budget.setBudget_date(rs.getDate("budget_date").toLocalDate());
        return budget;
    };

    // 총 예산 목록 조회
    public List<BudgetDomain> findAllBudgets(Long user_id, LocalDate budget_date) {
        String sql = """ 
            SELECT budget_id, user_id, category_id, expected_amount, budget_date 
            FROM budgets 
            WHERE user_id = ? AND budget_date = ?
        """;
        return jdbcTemplate.query(sql, budgetRowMapper, user_id, budget_date);
    }
    // 카테고리 예산 목록 조회
    public List<BudgetDomain> findAllBudgets(Long user_id, Long category_id, LocalDate budget_date) {
        String sql =
                """ 
                    SELECT budget_id, user_id, category_id, expected_amount, budget_date 
                    FROM budgets 
                    WHERE user_id = ? AND category_id = ? AND budget_date = ?
                """;
        return jdbcTemplate.query(sql, budgetRowMapper, user_id, category_id, budget_date);
    }

    // 예산 저장
    public void save(BudgetDomain budget) {
        String sql = """
                         INSERT INTO budgets 
                         (user_id, category_id, expected_amount, budget_date) 
                         VALUES (?, ?, ?, ?)
                     """;
        jdbcTemplate.update(sql,
                budget.getUser_id(),
                budget.getCategory_id(),
                budget.getExpected_amount(),
                budget.getBudget_date()
        );
    }

    // 예산 수정
    public void update(BudgetDomain budget) {
        String sql =
                    """
                        UPDATE budgets 
                        SET expected_amount = ? 
                        WHERE budget_id = ?
                    """;
        jdbcTemplate.update(sql,
                budget.getExpected_amount(),
                budget.getBudget_id()
        );
    }
}