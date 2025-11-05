package Project_Manager.AccountManager.repository;

import Project_Manager.AccountManager.dto.expenseDto.ExpenseDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class ExpenseRepository {
    private final JdbcTemplate jdbcTemplate;

    public ExpenseRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<ExpenseDto> amountByCategory = (rs, rowNum) -> {
        ExpenseDto dto = new ExpenseDto();

        dto.setCategoryName(rs.getString("category_name"));
        dto.setTotalAmount(rs.getLong("total_amount"));

        return dto;
    };

    //카테고리별 총예산
    public List<ExpenseDto> getTotalExpenseByCategory(long userId, LocalDate startDate, LocalDate endDate) {
        String sql = """
                SELECT
                    COALESCE(cat.category_name, '미분류') AS category_name,
                    SUM(c.amount) AS total_amount
                FROM
                    calendars c
                LEFT JOIN
                    categories cat ON c.category_id = cat.category_id
                WHERE
                    c.user_id = ?
                    AND c.entry_type = 'EXPENSE'
                    AND c.calendar_date BETWEEN ? AND ?
                GROUP BY
                    cat.category_name
                ORDER BY
                    total_amount DESC;
                """;

        return jdbcTemplate.query(sql, amountByCategory, userId, startDate, endDate);
    }


}

