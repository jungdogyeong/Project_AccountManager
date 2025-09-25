package Project_Manager.AccountManager.repository;

import Project_Manager.AccountManager.domain.ExpenseDomain;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class BudgetRepository {
    private final ExpenseDomain expenseDomain;
    JdbcTemplate jdbcTemplate;

    public BudgetRepository(ExpenseDomain expenseDomain) {
        this.expenseDomain = expenseDomain;
    }

    public int getTotalExpense(int member_id) {
        String sql = """
                   SELECT SUM(amount)
                   FROM expense
                   WHERE member_id = ? 
                """;

        Integer total = jdbcTemplate.queryForObject(sql, Integer.class, member_id);

        return total;
    }

//    public int getLikedCount(int boardId) {
//        String sql = "SELECT likeCount FROM board WHERE boardId = ?";
//        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, boardId);
//
//        return (count != null ? count : 0);
//    }

//    public void saveSchedule(Calendar calendar) {
//        String sql = """
//                    INSERT INTO schedules (scheduleId, scheduleTitle, scheduleContent, scheduleDate, userId)
//                    VALUES (?, ?, ?, ?, ?)
//                """;
//
//        jdbcTemplate.update(sql, calendar.getScheduleId(), calendar.getScheduleTitle(), calendar.getScheduleContent(), calendar.getScheduleDate(), calendar.getUserId());
//    }
}
